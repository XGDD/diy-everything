package com.calm.diyeverything.heart.interceptor.impl;

import com.calm.diyeverything.heart.interceptor.FileInterceptor;

import java.io.File;

public class FilePrintInterceptor implements FileInterceptor {
    @Override
    public void apply(File file) {
        System.out.println(file.getAbsolutePath());
    }
}
