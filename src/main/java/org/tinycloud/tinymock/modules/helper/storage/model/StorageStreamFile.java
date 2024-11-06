package org.tinycloud.tinymock.modules.helper.storage.model;

import java.io.*;


/**
 * @author liuxingyu01
 * @date 2021-07-09-9:15
 **/
public class StorageStreamFile extends StorageFile {
    private InputStream inputStream;


    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public long writeTo(OutputStream out) throws IOException {
        int ch;
        while ((ch = inputStream.read()) != -1) {
            out.write(ch);
        }
        return inputStream.available();
    }

    public long writeTo(File file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            return writeTo(out);
        }
    }

    public long writeTo(String filename) throws IOException {
        return writeTo(new File(filename));
    }
}
