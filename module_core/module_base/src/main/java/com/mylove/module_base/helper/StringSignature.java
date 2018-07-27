package com.mylove.module_base.helper;

import android.support.annotation.NonNull;

import com.bumptech.glide.load.Key;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2018/7/26.
 */

public class StringSignature implements Key {

    private String signV;

    public StringSignature(String signV) {
        this.signV = signV;
    }

    @Override
    public int hashCode() {
        return signV.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StringSignature that = (StringSignature) o;

        if (!signV.equals(that.signV)) {
            return false;
        }

        return true;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(signV.getBytes(CHARSET));
    }
}
