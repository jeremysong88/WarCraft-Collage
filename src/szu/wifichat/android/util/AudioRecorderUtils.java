package szu.wifichat.android.util;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;

/**录制声音类
 * @author ※範成功※
 * @date 2015-3-16
 * 
 */
public class AudioRecorderUtils {
    private static int SAMPLE_RATE_IN_HZ = 8000; // 采样率���

    /**
     * 实现录音和录像类对象
     */
    private MediaRecorder mMediaRecorder;
    private String mVoicePath;

    public AudioRecorderUtils() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
    }

    public void setVoicePath(String path, String filename) {
        this.mVoicePath = path + File.separator + filename + ".amr";
    }

    public String getVoicePath() {
        return this.mVoicePath;
    }

    public void start() throws IOException {
        File directory = new File(mVoicePath).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created");
        }
        // 设置录制视频源为MIC(话筒)
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置录制完成后音频的封装格式THREE_GPP为3gp.MPEG_4为mp4  
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 设置音频编码
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置音频的采样率
        mMediaRecorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
        mMediaRecorder.setOutputFile(mVoicePath);
        // 准备函数
        mMediaRecorder.prepare();
        mMediaRecorder.start();
    }

    public void stop() throws IOException {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mMediaRecorder != null) {
        	//说话的音量的大小
            return (mMediaRecorder.getMaxAmplitude());
        }
        else
            return 0;
    }
}