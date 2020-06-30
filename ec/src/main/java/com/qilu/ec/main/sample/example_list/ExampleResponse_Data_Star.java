package com.qilu.ec.main.sample.example_list;

public class ExampleResponse_Data_Star {
    /*
    "ID": 9,
                "Content": "KKK",
                "Images": ""
     */
    private String ID;
    private String Content;
    private String Images;

    public ExampleResponse_Data_Star(String ID,
                                     String content,
                                     String images) {
        this.ID = ID;
        Content = content;
        Images = images;
    }

    public String getID() {
        return ID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getImages() {
        return Images;
    }

}
