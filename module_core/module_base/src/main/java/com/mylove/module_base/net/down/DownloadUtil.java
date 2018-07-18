package com.mylove.module_base.net.down;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * Created by Will on 2017/3/11.
 */

public class DownloadUtil {

    public static final String ACTION_PROGRESS = DownConfig.APPLICATION_ID + "action_progress";
    public static final String ACTION_FINISHED = DownConfig.APPLICATION_ID + "action_finished";
    public static final String ACTION_PAUSED = DownConfig.APPLICATION_ID + "action_paused";
    public static final String ACTION_FILE_LENGTH_GET = DownConfig.APPLICATION_ID + "action_file_length_set";
    public static final String ACTION_FAILED = DownConfig.APPLICATION_ID + "action_failed";
    public static final String ACTION_NEW_TASK_ADD = DownConfig.APPLICATION_ID + "action_new_task_add";
    public static final String ACTION_RESUME = DownConfig.APPLICATION_ID + "action_resume";
    public static final String ACTION_START = DownConfig.APPLICATION_ID + "action_start";
    public static final String ACTION_REENQUEUE = DownConfig.APPLICATION_ID + "action_reenqueue";
    public static final String ACTION_CANCELED = DownConfig.APPLICATION_ID + "action_canceled";

    public static final String EXTRA_TASK_ID = DownConfig.APPLICATION_ID + "extra_task_id";
    public static final String EXTRA_ERROR_MSG = DownConfig.APPLICATION_ID + "extra_error_msg";

    static ExecutorService sExecutor;
    static Map<String, DownloadRecord> sRecordMap;
    static Semaphore sDownloadPermit;
    static int sThreadNum;

    private static DownloadUtil instance;
    private TaskDispatcher mTaskDispatcher;
    private LocalBroadcastManager mBroadcastManager;
    private DataHelper mDataHelper;

    public static final int STATE_INITIAL = 0;
    public static final int STATE_DOWNLOADING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_FINISHED = 3;
    public static final int STATE_CANCELED = 4;
    public static final int STATE_FAILED = 5;
    public static final int STATE_REENQUEUE = 6;

    final static int TIME_OUT = 5000;
    final static int DEFAULT_TASK_AMOUNT = 3;
    final static int DEFAULT_THREAD_AMOUNT = 3;

    private boolean initialized = false;

    private BroadcastReceiver mReceiver;

    public DownloadUtil init(Context context) {
        if (initialized) return instance;
        mBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        mDataHelper = new DataHelper(context.getApplicationContext());
        loadAll();
        initialized = true;
        return instance;
    }

    /**
     *
     * @param number
     * @return
     */
    public DownloadUtil setMaxConcurrentTask(int number) {
        if (initialized) return instance;
        sDownloadPermit = new Semaphore(number);
        return instance;
    }

    private DownloadUtil() {
        sRecordMap = new LinkedHashMap<String, DownloadRecord>();
        sExecutor = Executors.newCachedThreadPool();
        sDownloadPermit = new Semaphore(DEFAULT_TASK_AMOUNT);
        sThreadNum = DEFAULT_THREAD_AMOUNT;
        mTaskDispatcher = new TaskDispatcher();
        mTaskDispatcher.start();
    }

    public static DownloadUtil get() {
        if (instance == null) {
            synchronized (DownloadUtil.class) {
                if (instance == null)
                    instance = new DownloadUtil();
            }
        }
        return instance;
    }

    public Collection<DownloadRecord> getAllTasks(){
        return sRecordMap.values();
    }

    public void destroy() {
        mBroadcastManager.unregisterReceiver(mReceiver);
        mTaskDispatcher.quit();
        stopAllTask();
        saveAll();
        sRecordMap.clear();
        sRecordMap = null;
        sDownloadPermit = null;
        sExecutor = null;
        instance = null;
        initialized = false;
    }

    public void stopAllTask() {
        for (DownloadRecord record : sRecordMap.values()) {
            pause(record.getId());
        }
    }

    synchronized void saveRecord(DownloadRecord record) {
        mDataHelper.saveRecord(record);
    }

    public void saveAll() {
        mDataHelper.saveAllRecord(sRecordMap.values());
    }

    public void loadAll() {
        for (DownloadRecord record : mDataHelper.loadAllRecords()) {
            sRecordMap.put(record.getId(), record);
        }
    }

    /**
     * @param request
     * @return
     */
    public String enqueue(DownloadRequest request) {
        if (!checkRequest(request)) {
            return null;
        }
        DownloadRecord record = new DownloadRecord(request);
        sRecordMap.put(request.getId(), record);
        mTaskDispatcher.enqueueRecord(record);
        DownloadUtil.get().newTaskAdd(record);
        return request.getId();
    }

    private boolean checkRequest(DownloadRequest request) {
        if(request == null){
            return false;
        }
        if (sRecordMap.get(request.getId()) != null) {
            return false;
        }
        for (DownloadRecord record : sRecordMap.values()) {
            if (record.getDownloadDir().equals(request.getDownloadDir())
                    && record.getDownloadName().equals(request.getDownloadDir()))
                return false;
        }
        return true;
    }

    private void removeTask(DownloadRecord record){
        sRecordMap.remove(record.getId());
    }

    public boolean reEnqueue(String taskId) {
        if (sRecordMap.get(taskId) != null) {
            sRecordMap.get(taskId).setDownloadState(STATE_REENQUEUE);
            DownloadRecord record = sRecordMap.get(taskId);
            mTaskDispatcher.enqueueRecord(record);
            Intent intent = new Intent(ACTION_REENQUEUE);
            intent.putExtra(EXTRA_TASK_ID, taskId);
            mBroadcastManager.sendBroadcast(intent);
            DownloadListener listener = record.getRequest().getListener();
            if(listener != null){
                listener.onProgress(record);
            }
            return true;
        }
        return false;
    }

    /**
     * @param taskId
     */
    public boolean pause(String taskId) {
        DownloadRecord record = sRecordMap.get(taskId);
        if (record != null) {
            if (record.getDownloadState() == STATE_INITIAL
                    || record.getDownloadState() == STATE_PAUSED
                    || record.getDownloadState() == STATE_CANCELED
                    || record.getDownloadState() == STATE_FAILED
                    || record.getDownloadState() == STATE_FINISHED)
                return false;

            sRecordMap.get(taskId).setDownloadState(STATE_PAUSED);
            sDownloadPermit.release();
            Intent intent = new Intent(ACTION_PAUSED);
            intent.putExtra(EXTRA_TASK_ID, taskId);
            mBroadcastManager.sendBroadcast(intent);

            DownloadListener listener = record.getRequest().getListener();
            if(listener != null){
                listener.onPaused(record);
            }

            return true;
        }
        return false;
    }

    public boolean cancel(String taskId) {
        if (sRecordMap.get(taskId) != null) {
            sRecordMap.get(taskId).setDownloadState(STATE_CANCELED);
            sDownloadPermit.release();
            Intent intent = new Intent(ACTION_CANCELED);
            intent.putExtra(EXTRA_TASK_ID, taskId);
            mBroadcastManager.sendBroadcast(intent);
            DownloadRecord record = sRecordMap.get(taskId);
            DownloadListener listener = record.getRequest().getListener();
            if(listener != null){
                listener.onCanceled(record);
            }
            removeTask(record);
            return true;
        }
        return false;
    }

    /**
     * @param taskId
     */
    public boolean resume(String taskId) {
        if (sRecordMap.get(taskId) != null) {
            DownloadRecord record = sRecordMap.get(taskId);
            record.setDownloadState(STATE_DOWNLOADING);
            Intent intent = new Intent(ACTION_RESUME);
            intent.putExtra(EXTRA_TASK_ID, taskId);
            mBroadcastManager.sendBroadcast(intent);

            DownloadListener listener = record.getRequest().getListener();
            if(listener != null){
                listener.onResume(record);
            }

            for (int i = 0; i < record.getSubTaskList().size(); i++) {
                sExecutor.execute(record.getSubTaskList().get(i));
            }
            return true;
        }
        return false;
    }

    public void progressUpdated(DownloadRecord record) {
        Intent intent = new Intent(ACTION_PROGRESS);
        intent.putExtra(EXTRA_TASK_ID, record.getId());
        mBroadcastManager.sendBroadcast(intent);
        DownloadListener listener = record.getRequest().getListener();
        if(listener != null){
            listener.onProgress(record);
        }
    }

    public void taskFinished(DownloadRecord record) {
        sDownloadPermit.release();
        record.setDownloadState(STATE_FINISHED);
        saveRecord(record);
        Intent intent = new Intent(ACTION_FINISHED);
        intent.putExtra(EXTRA_TASK_ID, record.getId());
        mBroadcastManager.sendBroadcast(intent);
        DownloadListener listener = record.getRequest().getListener();
        if(listener != null){
            listener.onFinish(record);
        }
        removeTask(record);
    }

    public void fileLengthSet(DownloadRecord record) {
        Intent intent = new Intent(ACTION_FILE_LENGTH_GET);
        intent.putExtra(EXTRA_TASK_ID, record.getId());
        mBroadcastManager.sendBroadcast(intent);

        DownloadListener listener = record.getRequest().getListener();
        if(listener != null){
            listener.onFileLengthGet(record);
        }
    }

    public void downloadFailed(DownloadRecord record, String errorMsg) {
        sDownloadPermit.release();
        record.setDownloadState(STATE_FAILED);
        saveRecord(record);
        Intent intent = new Intent(ACTION_FAILED);
        intent.putExtra(EXTRA_TASK_ID, record.getId());
        intent.putExtra(EXTRA_ERROR_MSG, errorMsg);
        mBroadcastManager.sendBroadcast(intent);
        DownloadListener listener = record.getRequest().getListener();
        if(listener != null){
            listener.onFailed(record,errorMsg);
        }
        removeTask(record);
    }

    public void newTaskAdd(DownloadRecord record) {
        mDataHelper.saveRecord(record);
        Intent intent = new Intent(ACTION_NEW_TASK_ADD);
        intent.putExtra(EXTRA_TASK_ID, record.getId());
        mBroadcastManager.sendBroadcast(intent);
        DownloadListener listener = record.getRequest().getListener();
        if(listener != null){
            listener.onNewTaskAdd(record);
        }
    }

    public static DownloadRecord parseRecord(Intent intent) {
        String taskId = intent.getStringExtra(EXTRA_TASK_ID);
        return sRecordMap.get(taskId);
    }

    void start(DownloadRecord record) {
        record.reset();
        record.setDownloadState(STATE_DOWNLOADING);
        new DownloadTask().executeOnExecutor(sExecutor, record);
        Intent intent = new Intent(ACTION_START);
        intent.putExtra(EXTRA_TASK_ID, record.getId());
        mBroadcastManager.sendBroadcast(intent);
        DownloadListener listener = record.getRequest().getListener();
        if(listener != null){
            listener.onStart(record);
        }
    }

    public void registerListener(Context context, final DownloadListener listener) {
        if (listener == null)
            return;

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NEW_TASK_ADD);
        filter.addAction(ACTION_START);
        filter.addAction(ACTION_FILE_LENGTH_GET);
        filter.addAction(ACTION_PROGRESS);
        filter.addAction(ACTION_PAUSED);
        filter.addAction(ACTION_REENQUEUE);
        filter.addAction(ACTION_RESUME);
        filter.addAction(ACTION_FINISHED);
        filter.addAction(ACTION_FAILED);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                DownloadRecord record = DownloadUtil.parseRecord(intent);
                String action = intent.getAction();
                if(ACTION_NEW_TASK_ADD.equals(action)){
                	listener.onNewTaskAdd(record);
                }else if(ACTION_START.equals(action)){
                	listener.onStart(record);
                }else if(ACTION_FILE_LENGTH_GET.equals(action)){
                	listener.onFileLengthGet(record);
                }else if(ACTION_PROGRESS.equals(action)){
                	listener.onProgress(record);
                }else if(ACTION_PAUSED.equals(action)){
                	listener.onPaused(record);
                }else if(ACTION_RESUME.equals(action)){
                	listener.onResume(record);
                }else if(ACTION_REENQUEUE.equals(action)){
                	listener.onReEnqueue(record);
                }else if(ACTION_CANCELED.equals(action)){
                	listener.onCanceled(record);
                }else if(ACTION_FAILED.equals(action)){
                	listener.onFailed(record, intent.getStringExtra(EXTRA_ERROR_MSG));
                }else if(ACTION_FINISHED.equals(action)){
                	listener.onFinish(record);
                }
                
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, filter);
    }

    public static String getMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }
}
