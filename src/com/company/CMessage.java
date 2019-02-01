package com.company;

import java.io.Serializable;

public class CMessage extends Message implements Serializable {
    byte[] file = null;
    String fileName = null;
    String recipient = null;
    private static final long serialVersionUID = 69L;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
