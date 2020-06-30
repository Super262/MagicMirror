package com.qilu.ec.main.user;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.qilu.core.delegates.QiluDelegate;
import com.qilu.core.ec.R;
import com.qilu.core.net.RestClient;
import com.qilu.core.util.callback.CallbackManager;
import com.qilu.core.util.callback.CallbackType;
import com.qilu.core.util.callback.IGlobalCallback;
import com.qilu.core.util.storage.ImageHistoryHelper;
import com.qilu.ec.main.util.Image;
import com.qilu.ui.image.GlideTools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import hearsilent.discreteslider.DiscreteSlider;

@SuppressLint("ValidFragment")
public class DecorateFromStarDelegate extends QiluDelegate implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private IconTextView button_1;
    private IconTextView button_2;
    private IconTextView result_button;
    private String result_img_base64;
    private DiscreteSlider value;
    private RadioGroup radio_group;
    private RadioButton radioButton;
    private RadioButton radioButton_2;
    private int radio_1;
    private int radio_2;
    private CheckBox check_1;
    private CheckBox check_2;
    private CheckBox check_3;
    private boolean checkAll;
    private Button submit;

    //结果区域
    private LinearLayoutCompat resultLayout;
    private ImageView img_result;

    //保存待上传图片路径的字符串
    private String img_1_path = null;
    private String img_2_path = null;
    private String img1_base64;


    public DecorateFromStarDelegate(String img_Base64) {
        this.img1_base64 = img_Base64;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_decorate;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        button_1 = rootView.findViewById(R.id.button_1);
        button_2 = rootView.findViewById(R.id.button_2);
        result_button = rootView.findViewById(R.id.result_button);
        result_img_base64 = null;
        value = rootView.findViewById(R.id.value);
        radio_group = rootView.findViewById(R.id.radio_group);
        radio_1 = R.id.radio_1;
        radio_2 = R.id.radio_2;
        radioButton = rootView.findViewById(radio_1);
        radioButton_2 = rootView.findViewById(radio_2);
        radioButton.setChecked(true);
        check_1 = rootView.findViewById(R.id.check_1);
        check_2 = rootView.findViewById(R.id.check_2);
        check_3 = rootView.findViewById(R.id.check_3);
        checkAll = true;
        checkAll();
        submit = rootView.findViewById(R.id.submit);

        resultLayout = rootView.findViewById(R.id.resultLayout);
        img_result = rootView.findViewById(R.id.img_result);
        resultLayout.setVisibility(View.INVISIBLE);
        button_1.setVisibility(View.INVISIBLE);

        listenerRegister();
        checkToShowImg1();
    }

    private void checkToShowImg1() {
        File file = Image.base64ToFile(img1_base64);  //获取临时文件路径
        String path = file.getAbsolutePath();
        Log.i("文件路径", file.getAbsolutePath());
        img_1_path = path;
        DecorateFromStarDelegate temp = this;
        GlideTools.showImagewithGlide(temp, img_1_path, $(R.id.img));
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.i("isChecked", String.valueOf(isChecked));
            if (checkAll) {
                buttonView.setChecked(true);
            } else {
                if (!isChecked) {
                    //false时，只有当为最后一个选中时不可执行
                    if (buttonView.getId() == check_1.getId()) {
                        if (!check_2.isChecked() && !check_3.isChecked())
                            buttonView.setChecked(true);
                    } else if (buttonView.getId() == check_2.getId()) {
                        if (!check_1.isChecked() && !check_3.isChecked())
                            buttonView.setChecked(true);
                    } else if (buttonView.getId() == check_3.getId()) {
                        if (!check_1.isChecked() && !check_2.isChecked())
                            buttonView.setChecked(true);
                    }
                } else {
                    buttonView.setChecked(true);
                }
            }
        }
    };

    private void listenerRegister() {
        button_2.setOnClickListener(this);
        result_button.setOnClickListener(this);
        submit.setOnClickListener(this);
        radio_group.setOnCheckedChangeListener(this);
        check_1.setOnCheckedChangeListener(onCheckedChangeListener);
        check_2.setOnCheckedChangeListener(onCheckedChangeListener);
        check_3.setOnCheckedChangeListener(onCheckedChangeListener);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.button_2) {
//            position = POSITION_TWO;

            // 设置拍照后的动作
            DecorateFromStarDelegate temp = this;
            CallbackManager.getInstance().addCallback(CallbackType.ON_CROP, (IGlobalCallback<Uri>) args -> {
                // args是照片保存在硬盘上的地址
                if (args != null) {
                    img_2_path = args.getPath();
                    GlideTools.showImagewithGlide(temp, img_2_path, $(R.id.img_2));

                }
            });
            startCameraWithCheck();

        } else if (id == R.id.submit) {
            submitImage();
        } else if (id == R.id.result_button) {
            saveToLocal();
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == radio_1) {
            checkAll = true;
            checkAll();
        } else if (checkedId == radio_2) {
            checkAll = false;
            setCheckDefault();
        }
    }

    private void checkAll() {
        check_1.setChecked(true);
        check_2.setChecked(true);
        check_3.setChecked(true);
    }

    private void setCheckDefault() {
        check_3.setChecked(false);
        check_2.setChecked(false);
        check_1.setChecked(true);
    }

    private void submitImage() {
        sendRequest(img_1_path, img_2_path, value.getProgress(), getType());
    }

    private void saveToLocal() {
        if (result_img_base64 != null && !result_img_base64.equals("")) {
            Image.saveImageToGallery(getContext(), Image.base64ToBitmap(result_img_base64));
            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param image 图片的Base64
     * @return boolean
     */
    private boolean saveHistory(@Nullable String image) {
        ImageHistoryHelper imageHistoryHelper = new ImageHistoryHelper(getContext());
        SQLiteDatabase db = imageHistoryHelper.getWritableDatabase();
        String time = getTime();
        ContentValues values = new ContentValues();
        values.put(ImageHistoryHelper.IMAGE, image);
        values.put(ImageHistoryHelper.TIME, time);
        long result = db.insert(ImageHistoryHelper.TABLE_NAME, null, values);
        if (result > 0) {
            //增加成功
            Log.i("历史记录", "增加");
            db.close();
            return true;
        } else {
            Log.i("历史记录", "未增加");
            db.close();
            return false;
        }
    }

    private String getTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        Log.i("美妆时间", time);
        return time;
    }

    /**
     * 发送网络请求端,返回类型不确定，可能是图片相关类型
     */
    private void sendRequest(@Nullable String image1Path, @Nullable String image2Path, int levelDegree, String[] paramsValues) {
        if (image1Path != null && (!image1Path.isEmpty()) && image2Path != null && (!image2Path.isEmpty())) {
            Log.i("img1路径", image1Path);
            Log.i("img2路径", image2Path);
            // levelDegree: [0, 99]
            levelDegree += 1;
            float shade = ((float) levelDegree) / 100.0f;
            String[] paramsNames = {"local_flag", "eye_flag", "lip_flag", "face_flag"};

            RestClient.builder()
                    .url("http://106.13.96.60:5000/generate_makeup")
                    .file("example_image", image1Path)
                    .file("user_image", image2Path)
                    .params("shade_alpha", String.valueOf(shade))
                    .params(paramsNames[0], paramsValues[0])
                    .params(paramsNames[1], paramsValues[1])
                    .params(paramsNames[2], paramsValues[2])
                    .params(paramsNames[3], paramsValues[3])
                    .success(response -> {
                        Image.showResultImage(response, img_result);
                        resultLayout.setVisibility(View.VISIBLE);
                        result_img_base64 = response;   //用于下载到本地
                        if (saveHistory(response)) {
                            Toast.makeText(getContext(), "美妆成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "记录出错...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .failure(() -> Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show())
                    .error((code, msg) -> Toast.makeText(getContext(), "请求出错，Msg：" + msg + "\n" + "Code：" + code, Toast.LENGTH_LONG).show())
                    .loader(getContext())
                    .build()
                    .postWithFiles();
        }
    }

    private String[] getType() {
        String[] paramsValues = {"0", "0", "0", "0"};
        if (radioButton.isChecked())
            return paramsValues;
        else if (radioButton_2.isChecked()) {
            paramsValues[0] = "1";
            if (check_1.isChecked()) {
                paramsValues[1] = "1";
            }
            if (check_2.isChecked())
                paramsValues[2] = "1";
            if (check_3.isChecked())
                paramsValues[3] = "1";
            for (String paramsValue : paramsValues) {
                if (!paramsValue.equals("1")) {
                    return paramsValues;
                }
            }
            paramsValues[0] = "0";
        }
        return paramsValues;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (img_1_path != null && (!img_1_path.isEmpty())) {
            File file = new File(img_1_path);
            if (file.exists())
                file.delete();
        }
        if (img_2_path != null && (!img_2_path.isEmpty())) {
            File file = new File(img_2_path);
            if (file.exists())
                file.delete();
        }
    }
}
