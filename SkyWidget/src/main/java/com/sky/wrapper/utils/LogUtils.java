package com.sky.wrapper.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class LogUtils {
    private static String DIRPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Log";  //存放日志文件的所在路径
    //存放日志的文本名
    private static final String LOGNAME = String.format("log-%1$s.txt", DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()));

    private static final String INFORMAT = "yyyy-MM-dd HH:mm:ss"; //设置时间的格式
    private static boolean isWrite = false;   //isWrite:用于开关是否日志写入txt文件中
    private static boolean isDeBug = true;  //isDebug :是用来控制，是否打印日志

    private static final String tag = " -- sky -- ";
    public static int logLevel = Log.DEBUG;

    public static void setLogConfig(String path, boolean debugable, boolean writeable) {
        DIRPATH = path;
        isDeBug = debugable;
        isWrite = writeable;
    }

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return null;
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(LogUtils.class.getName())) {
                continue;
            }

            return "[" + Thread.currentThread().getId() + ": " + st.getFileName() + ":" + st.getLineNumber() + "]";
        }

        return null;
    }

    public static void info(String message) {
        if (logLevel <= Log.INFO) {
            String name = getFunctionName();
            String ls = (name == null ? message : (name + " - " + message));
            i(tag, ls);
        }
    }

    public static void i(String message) {
        info(message);
    }

    public static void verbose(String message) {
        if (logLevel <= Log.VERBOSE) {
            String name = getFunctionName();
            String ls = (name == null ? message : (name + " - " + message));
            Log.v(tag, ls);
        }
    }

    public static void v(String message) {
        verbose(message);
    }

    public static void warn(String message) {
        if (logLevel <= Log.WARN) {
            String name = getFunctionName();
            String ls = (name == null ? message : (name + " - " + message));
            Log.w(tag, ls);
        }
    }

    public static void w(String message) {
        warn(message);
    }

    public static void error(String message) {
        if (logLevel <= Log.ERROR) {
            String name = getFunctionName();
            String ls = (name == null ? message : (name + " - " + message));
            e(tag, ls);
        }
    }

    public static void error(Throwable ex) {
        if (logLevel <= Log.ERROR) {
            StringBuffer sb = new StringBuffer();
            String name = getFunctionName();
            StackTraceElement[] sts = ex.getStackTrace();

            if (name != null) {
                sb.append(name + " - " + ex + "\r\n");
            } else {
                sb.append(ex + "\r\n");
            }

            if (sts != null && sts.length > 0) {
                for (StackTraceElement st : sts) {
                    if (st != null) {
                        sb.append("[ " + st.getFileName() + ":" + st.getLineNumber() + " ]\r\n");
                    }
                }
            }
            e(tag, sb.toString());
        }
    }

    public static void e(String message) {
        error(message);
    }

    public static void e(Exception ex) {
        error(ex);
    }

    public static void debug(String message) {
        if (logLevel <= Log.DEBUG) {
            String name = getFunctionName();
            String ls = (name == null ? message : (name + " - " + message));
            Log.d(tag, ls);
        }
    }

    public static void d(String message) {
        debug(message);
    }

    /**
     * info等级的日志输出
     *
     * @param tag 标识
     * @param msg 内容
     * @return void 返回类型
     * @throws
     */
    public static void i(String tag, String msg) {
        if (isDeBug) {
            if (msg != null) {
                int segmentSize = 3 * 1024;
                long length = msg.length();
                if (length <= segmentSize) {// 长度小于等于限制直接打印
                    Log.i(tag, msg);
                } else {
                    while (msg.length() > segmentSize) {// 循环分段打印日志
                        String logContent = msg.substring(0, segmentSize);
                        msg = msg.replace(logContent, "");
                        Log.i(tag, logContent);
                    }
                    Log.i(tag, msg);// 打印剩余日志
                }
            } else {
                Log.i(tag, msg);
            }
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * error等级的日志输出
     *
     * @param tag 标识
     * @param msg 内容
     * @return void 返回类型
     */
    public static void e(String tag, String msg) {
        if (isDeBug) {
            Log.w(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * 用于把日志内容写入制定的文件
     *
     * @param @param tag 标识
     * @param @param msg 要输出的内容
     * @return void 返回类型
     * @throws
     */
    public static void write(String tag, String msg) {


        String path = createMkdirsAndFiles(DIRPATH, LOGNAME);
        if (TextUtils.isEmpty(path)) {
            return;
        }
        String log = DateFormat.format(INFORMAT, System.currentTimeMillis())
                + " "
                + tag
                + " --------> \n"
                + msg
                + "\n---------------------------------Separator---------------------------------\n";
        FileUtils.write2File(path, log, true);
    }

    public static String createMkdirsAndFiles(String path, String filename) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路径为空");
        }
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                throw new RuntimeException("创建文件夹不成功");
            }
        }
        File f = new File(file, filename);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("创建文件不成功");
            }
        }
        return f.getAbsolutePath();
    }

    /**
     * 用于把日志内容写入制定的文件
     *
     * @param ex 异常
     */
    public static void write(Throwable ex) {
        write("", exToString(ex));
    }

    /**
     * 把异常信息转化为字符串
     *
     * @param ex 异常信息
     * @return 异常信息字符串
     */
    private static String exToString(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        String result = writer.toString();
        return result;
    }
}