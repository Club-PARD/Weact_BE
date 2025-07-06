package com.pard.weact.postPhoto.dto;

import java.io.InputStream;

public class ImageFile {
    private final String hashedName;
    private final String contentType;
    private final long size;
    private final InputStream inputStream;

    public ImageFile(String hashedName, String contentType, long size, InputStream inputStream) {
        this.hashedName = hashedName;
        this.contentType = contentType;
        this.size = size;
        this.inputStream = inputStream;
    }

    public String getHashedName() { return hashedName; }
    public String getContentType() { return contentType; }
    public long getSize() { return size; }
    public InputStream getInputStream() { return inputStream; }
}
