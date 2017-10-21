package whitelife.win.localfiletransfer;

/**
 * Created by wuzefeng on 2017/10/21.
 */

public class FileRequest {


    /**
     * 文件地址
     */
    private String filePath;


    /**
     * 文件名字
     */
    private String fileName;


    /**
     * 命令
     */
    private int code;//0表示请求，1表示同意，2表示拒绝


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
