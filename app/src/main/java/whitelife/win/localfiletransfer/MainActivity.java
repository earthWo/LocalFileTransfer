package whitelife.win.localfiletransfer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.File;

import whitelife.win.socketlibrary.callback.MessageCallback;
import whitelife.win.socketlibrary.callback.SocketCallback;
import whitelife.win.socketlibrary.con.SocketConnect;
import whitelife.win.socketlibrary.config.SocketConfig;
import whitelife.win.socketlibrary.exception.SocketException;
import whitelife.win.socketlibrary.message.SocketMessage;
import whitelife.win.socketlibrary.message.SocketTextMessage;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText= (EditText) findViewById(R.id.et_address);
        SocketConnect.init(this);


    }

    public void connect(View v){
        SocketConfig.init(mEditText.getText().toString(),20006);
        SocketConnect.get().setSocketCallback(new SocketCallback() {
            @Override
            public void connected() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                        mEditText.setEnabled(false);
                    }
                });
            }

            @Override
            public void connectError(SocketException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                        mEditText.setEnabled(true);
                        close(null);
                    }
                });
            }

            @Override
            public void disconnected() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"连接断开",Toast.LENGTH_SHORT).show();
                        close(null);
                    }
                });
            }
        }).setDefaultMessageCallback(new MessageCallback() {
            @Override
            public void response(int messageId, SocketMessage message) {
                if(message instanceof SocketTextMessage){
                    FileRequest request= JSON.parseObject((message).getData(),FileRequest.class);
                    if(request.getCode()==0){
                        showDialog(request);
                    }else if(request.getCode()==1){
                        SocketConnect.get().sendFileMessage(new File(request.getFilePath()));
                        Toast.makeText(MainActivity.this, "文件发送", Toast.LENGTH_SHORT)
                                .show();
                    }else{
                        Toast.makeText(MainActivity.this, "发送被拒绝", Toast.LENGTH_SHORT)
                                .show();
                    }

                }else{

                }
            }

            @Override
            public void requestError(SocketMessage message, SocketException e) {

            }
        }).connect();
    }

    public void close(View v){
        SocketConnect.get().closeConnect();
    }


    private static int FILE_SELECT_CODE=1001;

    public void select(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    private void showDialog(final FileRequest fileRequest){

        AlertDialog alertDialog=new AlertDialog.Builder(this).setTitle("是否接受文件:"+fileRequest.getFileName()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileRequest.setCode(1);
                SocketConnect.get().sendTextMessage(JSON.toJSONString(fileRequest));

            }
        }).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileRequest.setCode(2);
                SocketConnect.get().sendTextMessage(JSON.toJSONString(fileRequest));
            }
        }).create();
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String filePath=FileUtil.getFileAbsolutePath(this,uri);
            String fileName=filePath.substring(filePath.lastIndexOf("/"));
            FileRequest request=new FileRequest();
            request.setCode(0);
            request.setFileName(fileName);
            request.setFilePath(filePath);
            SocketConnect.get().sendTextMessage(JSON.toJSONString(request));
            Toast.makeText(this, "已发送文件请求", Toast.LENGTH_SHORT)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
