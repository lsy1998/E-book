package com.example.ebook;

public class UploadBook {
    private String name;
    private int imageId;
    private String path;

    public UploadBook(String name,String path, int imageId) {
        this.name = name;
        this.imageId = imageId;
        this.path=path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }
}
