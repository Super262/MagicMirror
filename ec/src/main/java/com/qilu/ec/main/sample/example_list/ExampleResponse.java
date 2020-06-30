package com.qilu.ec.main.sample.example_list;

public class ExampleResponse {
    /*
    {
    "code": 200,
    "data": {
        "stars": [
            {
                "ID": 9,
                "Content": "KKK",
                "Images": "",
                "PublishTime": 1593435871,
                "LikeNum": 0,
                "CommentNum": 0,
                "ForwardNum": 0,
                "IsForward": 0,
                "PreviousID": 0
            }
        ]
    },
    "msg": "获取所有动态成功"
}
     */
    private int code;
    private ExampleResponse_Data data;
    private String msg;

    public ExampleResponse(int code, ExampleResponse_Data data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ExampleResponse_Data getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
