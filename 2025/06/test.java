import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.WindowManager;
import org.json.JSONObject;
import me.hd.wauxv.plugin.api.callback.PluginCallBack;
import java.io.File;
import java.net.URLEncoder;
import java.io.*;
import java.util.Random;

// 新增：定义多个appid用于小尾巴
private static final String[] appids = {
    "wxe3ad19e142df87b3",
    "wx1ebb9c41ccbfb6d4",
    "wx6d8030a2f43b09a2",
    "wxb0eef1f67b7a2949",
    "wx115bcff956fd0905",
    "wx3f4266934f0e29fb",
    "wx315ce2808c20cb43",
    "wx322bb520817c18e7",
    "wx7395b7ea7ae1cab7",
    "wx281a70a3d390bdf2",
    "wxa0104328eeb70938",
    "wx92e3210df60c2e11",
    "wx274f9e94ca7302a1",
    "wxa43341eed288d77b",
    "wxefa60233f28c2955",
    "wxcdc5278445b04d39",
    "wxb0eef1f67b7a2949",
    "wx6321d27140be32de",
    "wx4fee0da9380b6608",
    "wx92398516de814096"
};

private Random random = new Random();

// 新增：获取随机appid的方法
private String getRandomAppid() {
    return appids[random.nextInt(appids.length)];
}

// 修改后的群组开关存储路径
String groupSwitchPath = pluginDir + "/KEJIYU/group_switch";

// 新增：存储群组开关状态
void putGroupSwitch(String groupId, boolean enabled) {
    try {
        File file = new File(groupSwitchPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        
        JSONObject json;
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            json = new JSONObject(sb.toString());
        } else {
            json = new JSONObject();
        }
        
        json.put(groupId, enabled);
        FileWriter fw = new FileWriter(file);
        fw.write(json.toString());
        fw.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// 新增：获取群组开关状态
boolean getGroupSwitch(String groupId) {
    try {
        File file = new File(groupSwitchPath);
        if (!file.exists()) {
            return false;
        }
        
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        
        JSONObject json = new JSONObject(sb.toString());
        return json.optBoolean(groupId, false);
    } catch (Exception e) {
        return false;
    }
}

void showHelpDialog() {
    Activity activity = getTopActivity();
    if (activity == null || activity.isFinishing()) {
        Log.e("KEJIYU", "Activity不可用");
        return;
    }

    Runnable dialogRunnable = new Runnable() {
        public void run() {
            try {
                SpannableStringBuilder helpMessage = new SpannableStringBuilder();
                
                // 功能说明标题
                appendGoldText(helpMessage, "功能说明：\n\n");
                
                // 群组开关说明
                appendGoldText(helpMessage, "【群组开关】\n");
                appendGoldText(helpMessage, "1. 开启功能: 长按输入「#开启」\n");
                appendGoldText(helpMessage, "2. 关闭功能: 长按输入「#关闭」\n\n");
                
                // 操作步骤
                appendGoldText(helpMessage, "操作步骤：\n");
                
                // 第一条操作步骤设置为浅红色
                int lightRed = Color.parseColor("#FF6666");
                int startStep1 = helpMessage.length();
                helpMessage.append("1. 本弹窗可以上下滑动查看\n");
                int endStep1 = helpMessage.length();
                helpMessage.setSpan(new ForegroundColorSpan(lightRed), 
                                   startStep1, endStep1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                
                // 其他操作步骤保持金色
                appendGoldText(helpMessage, "2. 在聊天框输入触发词，然后长按发送（不要点击发送）\n");
                appendGoldText(helpMessage, "3. 系统会自动获取并发送内容\n");
                appendGoldText(helpMessage, "4. 群聊触发，格式:@账户#关键词\n\n");
                
                // 文字类功能
                appendGoldText(helpMessage, "【文字类】\n");
                appendGoldText(helpMessage, "1. 舔狗语录：输入「舔狗」\n");
                appendGoldText(helpMessage, "2. 问候语：输入「问候」\n");
                appendGoldText(helpMessage, "3. 童锦程经典语录：输入「名言」\n");
                appendGoldText(helpMessage, "4. 美句摘抄：输入「美句」\n");
                appendGoldText(helpMessage, "5. 随机美食：输入「吃什么」\n");
                appendGoldText(helpMessage, "6. KFC文案：输入「kfc」\n");
                appendGoldText(helpMessage, "7. 历史上的今天：输入「历史」\n");
                appendGoldText(helpMessage, "8. 世界人口：输入「世界人口」响应较慢\n");
                appendGoldText(helpMessage, "9. 天气查询：输入「天气+城市」如「天气北京」\n");
                appendGoldText(helpMessage, "10. 十宗罪语录：输入「十宗罪」\n");
                appendGoldText(helpMessage, "11. 土味情话：输入「土味情话」\n");
                appendGoldText(helpMessage, "12. 骚话：输入「骚话」\n");
                appendGoldText(helpMessage, "13. 怼人语录：输入「怼」\n\n");
                
                // 图片类功能
                appendGoldText(helpMessage, "【图片类】\n");
                appendGoldText(helpMessage, "1. 每日晨报：输入「晨报」\n");
                appendGoldText(helpMessage, "2. 腹肌图片：输入「腹肌」\n");
                appendGoldText(helpMessage, "3. 三次元图片：输入「三次元」🔞\n");
                appendGoldText(helpMessage, "8. 二次元图片：输入「二次元」🔞\n");
                appendGoldText(helpMessage, "4. 黑丝图片：输入「黑丝」\n");
                appendGoldText(helpMessage, "5. 白丝图片：输入「白丝」\n");
                appendGoldText(helpMessage, "6. 动漫图片：输入「动漫」\n");
                appendGoldText(helpMessage, "7. 买家秀：输入「买家秀」\n");
                appendGoldText(helpMessage, "8. 龙图：输入「龙图」\n\n");

                
                // 视频类功能
                appendGoldText(helpMessage, "【视频类】\n");
                appendGoldText(helpMessage, "1. 小姐姐视频：输入「小姐姐」\n");
                appendGoldText(helpMessage, "2. COS视频：输入「cos」\n");
                appendGoldText(helpMessage, "3. 穿搭视频：输入「穿搭」\n");
                appendGoldText(helpMessage, "4. 玉足视频：输入「玉足」\n");
                appendGoldText(helpMessage, "5. 吊带视频：输入「吊带」\n");
                appendGoldText(helpMessage, "6. 萝莉视频：输入「萝莉」\n");
                appendGoldText(helpMessage, "7. 变装视频：输入「变装」\n\n");
                
                // 其他功能
                appendGoldText(helpMessage, "【其他】\n");
                appendGoldText(helpMessage, "1. 赞助：输入「赞助」\n");
                appendGoldText(helpMessage, "2. 帮助：输入「帮助」\n\n");
                
                // 更新日志标题
                int brightGreen = Color.parseColor("#00FF00");
                int startLog = helpMessage.length();
                helpMessage.append("【更新日志】\n");
                int endLog = helpMessage.length();
                helpMessage.setSpan(new ForegroundColorSpan(brightGreen), 
                                   startLog, endLog, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                
                // 更新日志内容
                int startLog1 = helpMessage.length();
                helpMessage.append("- 2025-07-12：新增多尾巴功能，发送图片带随机小尾巴\n");
                helpMessage.append("- 2025-07-12：新增二次元图片功能，优化视频类接口\n");
                helpMessage.append("- 2025-07-12：统一视频类跟图片类的文件格式\n");
                helpMessage.append("- 2025-07-12：新增图片类龙图\n");
                int endLog1 = helpMessage.length();
                helpMessage.setSpan(new ForegroundColorSpan(brightGreen), 
                                   startLog1, endLog1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // 创建一个可滚动的视图
                ScrollView scrollableView = new ScrollView(activity);
                scrollableView.setBackgroundColor(Color.TRANSPARENT);
                
                TextView messageView = new TextView(activity);
                messageView.setText(helpMessage);
                messageView.setTextSize(16);
                messageView.setPadding(40, 20, 40, 20);
                messageView.setGravity(Gravity.START);
                messageView.setBackgroundColor(Color.TRANSPARENT);
                scrollableView.addView(messageView);
                
                // 使用默认主题
                AlertDialog dialog = new AlertDialog.Builder(activity).create();
                
                TextView customTitle = new TextView(activity);
                customTitle.setText("功能帮助");
                customTitle.setTextColor(Color.BLUE);
                customTitle.setTextSize(20);
                customTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                customTitle.setGravity(Gravity.CENTER);
                customTitle.setPadding(0, 20, 0, 20);
                customTitle.setBackgroundColor(Color.TRANSPARENT);
                dialog.setCustomTitle(customTitle);
                
                dialog.setView(scrollableView);
                
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                
                dialog.show();
                
                // 确保透明背景
                UIX(dialog, Color.BLUE, Color.parseColor("#4CAF50"));
            } catch (Exception e) {
                Log.e("KEJIYU", "对话框创建错误", e);
                sendText(getTargetTalker(), "帮助功能暂时不可用，请稍后再试");
            }
        }
    };

    if (Looper.myLooper() == Looper.getMainLooper()) {
        dialogRunnable.run();
    } else {
        activity.runOnUiThread(dialogRunnable);
    }
}

void appendGoldText(SpannableStringBuilder builder, String text) {
    int start = builder.length();
    builder.append(text);
    int end = builder.length();
    builder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD700")),
                   start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
}

// 修改后的UIX方法
void UIX(Dialog dialog, int titleColor, int buttonColor) {
    Window dialogWindow = dialog.getWindow();
    if (dialogWindow != null) {
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ColorDrawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        dialogWindow.setBackgroundDrawable(transparentDrawable);
        
        View contentView = dialogWindow.findViewById(android.R.id.content);
        if (contentView != null) {
            contentView.setBackgroundColor(Color.TRANSPARENT);
        }
        
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (button != null) {
            button.setTextColor(buttonColor);
            button.setTextSize(16);
        }
        
        if (dialog.getCustomTitle() != null) {
            TextView titleView = (TextView) dialog.getCustomTitle();
            titleView.setTextColor(titleColor);
            titleView.setGravity(Gravity.CENTER);
        }
    }
}

// 下载和发送图片的通用方法（带小尾巴）
void downloadAndSendImage(String talker, String type, String url) {
    // 统一图片命名为KEJIYU.jpg
    String filePath = pluginDir + "/KEJIYU.jpg";
    
    // 删除已存在的图片
    File oldImage = new File(filePath);
    if (oldImage.exists()) {
        oldImage.delete();
    }
    
    download(url, filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                String appid = getRandomAppid();
                sendImage(talker, file.getAbsolutePath(), appid);
            } else {
                sendText(talker, "图片无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "图片获取失败，请重试");
        }
    });
}

// 买家秀图片功能（带小尾巴）
void downloadAndSendBuyerShowImage(String talker) {
    // 统一图片命名为KEJIYU.jpg
    String filePath = pluginDir + "/KEJIYU.jpg";
    
    // 删除已存在的图片
    File oldImage = new File(filePath);
    if (oldImage.exists()) {
        oldImage.delete();
    }
    
    download("http://api.yujn.cn/api/mjx.php?", filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                String appid = getRandomAppid();
                sendImage(talker, file.getAbsolutePath(), appid);
            } else {
                sendText(talker, "买家秀图片无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "买家秀图片获取失败，请重试");
        }
    });
}

// 土味情话功能
void getTwQingHua(String talker) {
    new Thread(new Runnable() {
        public void run() {
            try {
                String apiUrl = "http://api.yujn.cn/api/qinghua.php?";
                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                
                java.io.InputStream is = conn.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                sendText(talker, "\n" + response.toString());
            } catch (Exception e) {
                sendText(talker, "土味情话获取失败，请重试");
            }
        }
    }).start();
}

// 小姐姐视频功能
void downloadAndSendXjjVideo(String talker) {
    // 统一视频命名为KEJIYU.mp4
    String filePath = pluginDir + "/KEJIYU.mp4";
    
    // 删除已存在的视频
    File oldVideo = new File(filePath);
    if (oldVideo.exists()) {
        oldVideo.delete();
    }
    
    String apiUrl = "http://api.yujn.cn/api/xjj.php?type=video";
    
    download(apiUrl + "&t=" + System.currentTimeMillis(), filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                sendVideo(talker, file.getAbsolutePath());
            } else {
                sendText(talker, "小姐姐视频无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "小姐姐视频下载失败");
        }
    });
}

// 黑丝图片功能（带小尾巴）
void downloadAndSendHeiSiImage(String talker) {
    // 统一图片命名为KEJIYU.jpg
    String filePath = pluginDir + "/KEJIYU.jpg";
    
    // 删除已存在的图片
    File oldImage = new File(filePath);
    if (oldImage.exists()) {
        oldImage.delete();
    }
    
    download("http://api.yujn.cn/api/heisi.php?", filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                String appid = getRandomAppid();
                sendImage(talker, file.getAbsolutePath(), appid);
            } else {
                sendText(talker, "黑丝图片无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "黑丝图片获取失败，请重试");
        }
    });
}

// 动漫图片功能（带小尾巴）
void downloadAndSendACGImage(String talker) {
    // 统一图片命名为KEJIYU.jpg
    String filePath = pluginDir + "/KEJIYU.jpg";
    
    // 删除已存在的图片
    File oldImage = new File(filePath);
    if (oldImage.exists()) {
        oldImage.delete();
    }
    
    String apiUrl = "https://api.yujn.cn/api/gzl_ACG.php?type=image&form=pc";
    
    download(apiUrl, filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                String appid = getRandomAppid();
                sendImage(talker, file.getAbsolutePath(), appid);
            } else {
                sendText(talker, "动漫图片无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "动漫图片获取失败，请重试");
        }
    });
}

// 吊带视频功能
void downloadAndSendDiaoDaiVideo(String talker) {
    // 统一视频命名为KEJIYU.mp4
    String filePath = pluginDir + "/KEJIYU.mp4";
    
    // 删除已存在的视频
    File oldVideo = new File(filePath);
    if (oldVideo.exists()) {
        oldVideo.delete();
    }
    
    String apiUrl = "http://api.yujn.cn/api/diaodai.php?type=video";
    
    download(apiUrl + "&t=" + System.currentTimeMillis(), filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                sendVideo(talker, file.getAbsolutePath());
            } else {
                sendText(talker, "吊带视频无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "吊带视频下载失败");
        }
    });
}

// COS视频功能
void downloadAndSendCOSVideo(String talker) {
    // 统一视频命名为KEJIYU.mp4
    String filePath = pluginDir + "/KEJIYU.mp4";
    
    // 删除已存在的视频
    File oldVideo = new File(filePath);
    if (oldVideo.exists()) {
        oldVideo.delete();
    }
    
    String apiUrl = "http://api.yujn.cn/api/COS.php?type=video";
    
    download(apiUrl + "&t=" + System.currentTimeMillis(), filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                sendVideo(talker, file.getAbsolutePath());
            } else {
                sendText(talker, "COS视频无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "COS视频下载失败");
        }
    });
}

// 萝莉视频功能
void downloadAndSendLuoLiVideo(String talker) {
    // 统一视频命名为KEJIYU.mp4
    String filePath = pluginDir + "/KEJIYU.mp4";
    
    // 删除已存在的视频
    File oldVideo = new File(filePath);
    if (oldVideo.exists()) {
        oldVideo.delete();
    }
    
    String apiUrl = "http://api.yujn.cn/api/luoli.php?type=video";
    
    download(apiUrl + "&t=" + System.currentTimeMillis(), filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                sendVideo(talker, file.getAbsolutePath());
            } else {
                sendText(talker, "萝莉视频无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "萝莉视频下载失败");
        }
    });
}

// 世界人口数据
void getWorldPopulation(String talker) {
    new Thread(new Runnable() {
        public void run() {
            try {
                String apiUrl = "http://api.yujn.cn/api/sjrk.php?type=text";
                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                
                java.io.InputStream is = conn.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                sendText(talker, "世界人口数据：\n" + response.toString());
            } catch (Exception e) {
                sendText(talker, "世界人口数据获取失败");
            }
        }
    }).start();
}

// 天气查询
void getWeather(String talker, String city) {
    new Thread(new Runnable() {
        public void run() {
            try {
                String encodedCity = URLEncoder.encode(city, "UTF-8");
                String apiUrl = "http://api.yujn.cn/api/tianqi.php?msg=" + encodedCity + "&b=1";
                
                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                
                java.io.InputStream is = conn.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                sendText(talker, "天气信息：\n" + response.toString());
            } catch (Exception e) {
                sendText(talker, "天气查询失败，请检查城市名称");
            }
        }
    }).start();
}

// 穿搭视频功能
void downloadAndSendChuandaVideo(String talker) {
    // 统一视频命名为KEJIYU.mp4
    String filePath = pluginDir + "/KEJIYU.mp4";
    
    // 删除已存在的视频
    File oldVideo = new File(filePath);
    if (oldVideo.exists()) {
        oldVideo.delete();
    }
    
    String apiUrl = "http://api.yujn.cn/api/chuanda.php?type=video";
    
    download(apiUrl + "&t=" + System.currentTimeMillis(), filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                sendVideo(talker, file.getAbsolutePath());
            } else {
                sendText(talker, "穿搭视频无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "穿搭视频下载失败");
        }
    });
}

// 玉足视频功能
void downloadAndSendYuZuVideo(String talker) {
    // 统一视频命名为KEJIYU.mp4
    String filePath = pluginDir + "/KEJIYU.mp4";
    
    // 删除已存在的视频
    File oldVideo = new File(filePath);
    if (oldVideo.exists()) {
        oldVideo.delete();
    }
    
    String apiUrl = "http://api.yujn.cn/api/jpmt.php?type=video";
    
    download(apiUrl + "&t=" + System.currentTimeMillis(), filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                sendVideo(talker, file.getAbsolutePath());
            } else {
                sendText(talker, "玉足视频无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "玉足视频下载失败");
        }
    });
}

// 龙图图片（带小尾巴）
void downloadAndSendLongImage(String talker) {
    String filePath = pluginDir + "/KEJIYU.jpg";
    File oldImage = new File(filePath);
    if (oldImage.exists()) {
        oldImage.delete();
    }

    download("https://api.yujn.cn/api/long.php?type=image", filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                String appid = getRandomAppid();
                sendImage(talker, file.getAbsolutePath(), appid);
            } else {
                sendText(talker, "龙图无效，请重试");
                if (file.exists()) file.delete();
            }
        }

        public void onError(Exception e) {
            sendText(talker, "龙图获取失败，请重试");
        }
    });
}


// 变装视频功能
void downloadAndSendBianZhuangVideo(String talker) {
    // 统一视频命名为KEJIYU.mp4
    String filePath = pluginDir + "/KEJIYU.mp4";
    
    // 删除已存在的视频
    File oldVideo = new File(filePath);
    if (oldVideo.exists()) {
        oldVideo.delete();
    }
    
    String apiUrl = "http://api.yujn.cn/api/bianzhuang.php?";
    
    download(apiUrl + "&t=" + System.currentTimeMillis(), filePath, null, new PluginCallBack.DownloadCallback() {
        public void onSuccess(File file) {
            if (file.exists() && file.length() > 1024) {
                sendVideo(talker, file.getAbsolutePath());
            } else {
                sendText(talker, "变装视频无效，请重试");
                if (file.exists()) file.delete();
            }
        }
        
        public void onError(Exception e) {
            sendText(talker, "变装视频下载失败");
        }
    });
}

// 十宗罪语录功能
void getSZZQuote(String talker) {
    new Thread(new Runnable() {
        public void run() {
            try {
                String apiUrl = "http://api.yujn.cn/api/szz.php?";
                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                
                java.io.InputStream is = conn.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                sendText(talker, "《十宗罪》语录：\n" + response.toString());
            } catch (Exception e) {
                sendText(talker, "十宗罪语录获取失败，请重试");
            }
        }
    }).start();
}

// 骚话功能
void getSaoHua(String talker) {
    new Thread(new Runnable() {
        public void run() {
            try {
                String apiUrl = "https://v.api.aa1.cn/api/api-saohua/index.php?type=json";
                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                
                java.io.InputStream is = conn.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                JSONObject jsonObject = new JSONObject(response.toString());
                String saohua = jsonObject.getString("saohua");
                
                sendText(talker, saohua);
            } catch (Exception e) {
                sendText(talker, "骚话获取失败，请重试");
            }
        }
    }).start();
}

// 白丝图片功能（带小尾巴）
void downloadAndSendBaiSiImage(String talker) {
    new Thread(new Runnable() {
        public void run() {
            try {
                String apiUrl = "https://v2.api-m.com/api/baisi?";
                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                
                java.io.InputStream is = conn.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                JSONObject jsonObject = new JSONObject(response.toString());
                int code = jsonObject.getInt("code");
                if (code == 200) {
                    String imageUrl = jsonObject.getString("data");
                    downloadAndSendImage(talker, "白丝", imageUrl);
                } else {
                    sendText(talker, "白丝图片获取失败");
                }
            } catch (Exception e) {
                sendText(talker, "白丝图片获取失败");
            }
        }
    }).start();
}

// 怼人语录功能
void getDuiRenYuLu(String talker) {
    new Thread(new Runnable() {
        public void run() {
            try {
                String apiUrl = "https://api.zxz.ee/api/duiren/";
                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                
                java.io.InputStream is = conn.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                sendText(talker, response.toString());
            } catch (Exception e) {
                sendText(talker, "怼人语录获取失败，请重试");
            }
        }
    }).start();
}

// 统一关键词处理方法
boolean handleKeywordCommand(String talker, String keyword) {
    if ("帮助".equals(keyword)) {
        showHelpDialog();
        return true;
    }
    
    if ("赞助".equals(keyword)) {
        sendText(talker, "#付款:KEJIYU(KEJIYU)");
        return true;
    }
    
    // ======================= 文字类功能 =======================
    if ("舔狗".equals(keyword)) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String apiUrl = "https://api.ahfi.cn/api/tgapi";
                    java.net.URL url = new java.net.URL(apiUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    
                    java.io.InputStream is = conn.getInputStream();
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    sendText(talker, response.toString());
                } catch (Exception e) {
                    sendText(talker, "舔狗语录获取失败，请重试");
                }
            }
        }).start();
        return true;
    }
    
    if ("问候".equals(keyword)) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String apiUrl = "https://api.ahfi.cn/api/getGreetingMessage";
                    java.net.URL url = new java.net.URL(apiUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    
                    java.io.InputStream is = conn.getInputStream();
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    sendText(talker, response.toString());
                } catch (Exception e) {
                    sendText(talker, "问候语获取失败，请重试");
                }
            }
        }).start();
        return true;
    }
    
    if ("名言".equals(keyword)) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String apiUrl = "https://api.ahfi.cn/api/zsyjdyl";
                    java.net.URL url = new java.net.URL(apiUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    
                    java.io.InputStream is = conn.getInputStream();
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    sendText(talker, response.toString());
                } catch (Exception e) {
                    sendText(talker, "童锦程语录获取失败，请重试");
                }
            }
        }).start();
        return true;
    }
    
    if ("美句".equals(keyword)) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String apiUrl = "https://api.ahfi.cn/api/bsnts";
                    java.net.URL url = new java.net.URL(apiUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    
                    java.io.InputStream is = conn.getInputStream();
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    sendText(talker, response.toString());
                } catch (Exception e) {
                    sendText(talker, "美句获取失败，请重试");
                }
            }
        }).start();
        return true;
    }
    
    if ("吃什么".equals(keyword)) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String apiUrl = "https://api.ahfi.cn/api/csm";
                    java.net.URL url = new java.net.URL(apiUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    
                    java.io.InputStream is = conn.getInputStream();
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String foodName = jsonObject.getString("food");
                    
                    sendText(talker, "今日美食推荐：\n" + foodName);
                } catch (Exception e) {
                    sendText(talker, "美食推荐获取失败，请重试");
                }
            }
        }).start();
        return true;
    }
    
    if ("kfc".equalsIgnoreCase(keyword)) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String apiUrl = "https://api.ahfi.cn/api/kfcv50";
                    java.net.URL url = new java.net.URL(apiUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    
                    java.io.InputStream is = conn.getInputStream();
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    sendText(talker, response.toString());
                } catch (Exception e) {
                    sendText(talker, "KFC文案获取失败，请重试");
                }
            }
        }).start();
        return true;
    }
    
    if ("历史".equals(keyword)) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String apiUrl = "https://api.ahfi.cn/api/lsjt";  
                    java.net.URL url = new java.net.URL(apiUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    
                    java.io.InputStream is = conn.getInputStream();
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is, "UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    reader.close();
                    
                    sendText(talker, "历史上的今天：\n" + response.toString().trim());
                } catch (Exception e) {
                    sendText(talker, "历史事件获取失败，请重试");
                }
            }
        }).start();
        return true;
    }
    
    if ("世界人口".equals(keyword)) {
        getWorldPopulation(talker);
        return true;
    }
    
    if (keyword.startsWith("天气")) {
        String city = keyword.replace("天气", "").trim();
        if (city.isEmpty()) {
            sendText(talker, "请输入城市名称，例如：天气北京");
        } else {
            getWeather(talker, city);
        }
        return true;
    }
    
    if ("十宗罪".equals(keyword)) {
        getSZZQuote(talker);
        return true;
    }
    
    if ("土味情话".equals(keyword)) {
        getTwQingHua(talker);
        return true;
    }
    
    if ("骚话".equals(keyword)) {
        getSaoHua(talker);
        return true;
    }
    
    if ("怼".equals(keyword)) {
        getDuiRenYuLu(talker);
        return true;
    }
    
    // ======================= 图片类功能 =======================
    if ("晨报".equals(keyword)) {
        downloadAndSendImage(talker, "晨报", "https://api.ahfi.cn/api/MorningNews");
        return true;
    }
    
    if ("腹肌".equals(keyword)) {
        downloadAndSendImage(talker, "腹肌", "http://api.yujn.cn/api/fujiimg.php?");
        return true;
    }
    
    if ("三次元".equals(keyword)) {
        downloadAndSendImage(talker, "三次元", "http://ynx.fremoe.site/API/R18_2");
        return true;
    }
    
    if ("二次元".equals(keyword)) { 
        downloadAndSendImage(talker, "二次元", "http://ynx.fremoe.site/API/R18");
        return true;
    }
    
    if ("龙图".equals(keyword)) {
    downloadAndSendLongImage(talker);
    return true;
    }

    
    if ("黑丝".equals(keyword)) {
        downloadAndSendHeiSiImage(talker);
        return true;
    }
    
    if ("动漫".equals(keyword)) {
        downloadAndSendACGImage(talker);
        return true;
    }
    
    if ("买家秀".equals(keyword)) {
        downloadAndSendBuyerShowImage(talker);
        return true;
    }
    
    if ("白丝".equals(keyword)) {
        downloadAndSendBaiSiImage(talker);
        return true;
    }
    
    // ======================= 视频类功能 =======================
    if ("小姐姐".equals(keyword)) {
        downloadAndSendXjjVideo(talker);
        return true;
    }
    
    if ("cos".equals(keyword)) {
        downloadAndSendCOSVideo(talker);
        return true;
    }
    
    if ("穿搭".equals(keyword)) {
        downloadAndSendChuandaVideo(talker);
        return true;
    }
    
    if ("玉足".equals(keyword)) {
        downloadAndSendYuZuVideo(talker);
        return true;
    }
    
    if ("吊带".equals(keyword)) {
        downloadAndSendDiaoDaiVideo(talker);
        return true;
    }
    
    if ("萝莉".equals(keyword)) {
        downloadAndSendLuoLiVideo(talker);
        return true;
    }
    
    if ("变装".equals(keyword)) {
        downloadAndSendBianZhuangVideo(talker);
        return true;
    }
    
    return false;
}

boolean onLongClickSendBtn(String text) {
    String trimmedText = text.trim();
    String group = getTargetTalker();
    
    if ("#开启".equals(trimmedText)) {
        putGroupSwitch(group, true);
        showOperationResultDialog("开启成功", "");
        return true;
    } else if ("#关闭".equals(trimmedText)) {
        putGroupSwitch(group, false);
        showOperationResultDialog("关闭成功", "");
        return true;
    }
    
    return handleKeywordCommand(group, trimmedText);
}

// 显示操作结果的对话框
void showOperationResultDialog(String title, String message) {
    Activity activity = getTopActivity();
    if (activity == null || activity.isFinishing()) return;
    
    activity.runOnUiThread(new Runnable() {
        public void run() {
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setBackgroundColor(Color.TRANSPARENT);

            TextView customTitle = new TextView(activity);
            customTitle.setText(title);
            customTitle.setTextColor(Color.parseColor("#FFD700"));
            customTitle.setTextSize(18);
            customTitle.setGravity(Gravity.CENTER);
            customTitle.setPadding(10, 10, 10, 10);

            TextView customMessage = new TextView(activity);
            customMessage.setText(message);
            customMessage.setTextColor(Color.parseColor("#FFD700"));
            customMessage.setTextSize(16);
            customMessage.setGravity(Gravity.CENTER);
            customMessage.setPadding(10, 10, 10, 10);

            layout.addView(customTitle);
            layout.addView(customMessage);

            AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
            
            dialog.show();
            
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(Color.parseColor("#4CAF50"));
            positiveButton.setTextSize(16);
            
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    });
}

void onHandleMsg(Object msgInfoBean) {
    try {
        if (msgInfoBean.isSend()) return;
        if (msgInfoBean.isText()) {
            String c = msgInfoBean.getContent();
            String t = msgInfoBean.getTalker();
            if (msgInfoBean.isAtMe()) {
                if (c.contains("@全体成员")) {
                    return;
                }
                String keyword = c.replace("@KEJIYU", "").trim();
                if (keyword.isEmpty()) {
                    sendText(t, "请输入关键词，例如：@KEJIYU #舔狗");
                    return;
                }
                int hashIndex = keyword.indexOf('#');
                if (hashIndex != -1) {
                    keyword = keyword.substring(hashIndex + 1).trim();
                } else {
                    return;
                }
                if (keyword.isEmpty()) {
                    sendText(t, "请输入关键词，例如：@KEJIYU #舔狗");
                    return;
                }
                
                if ("帮助".equals(keyword)) {
                    sendText(t, "帮助功能已调整为仅支持长按触发，请长按输入框旁边的发送按钮并输入「帮助」来查看帮助信息。");
                    return;
                }
                
                if (msgInfoBean.isGroupChat() && !getGroupSwitch(t)) {
                    if ("#开启".equals(keyword)) {
                        putGroupSwitch(t, true);
                        showOperationResultDialog("开启成功", "");
                    } else {
                        sendText(t, "KEJIYU功能在此群已关闭，请输入#开启启用");
                    }
                    return;
                }
                
                if (!handleKeywordCommand(t, keyword)) {
                    sendText(t, "未知指令，请输入正确的关键词");
                }
            }
        }
    } catch (Exception e) {
        Log.e("KEJIYU", "消息处理错误", e);
    }
}