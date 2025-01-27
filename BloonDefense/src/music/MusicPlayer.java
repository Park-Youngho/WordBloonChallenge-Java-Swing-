package music;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip musicClip; // 배경음악을 재생하는 Clip 객체
    private Clip popClip;  // 효과음을 재생하는 Clip 객체
    private File musicFile;  // 배경음악 파일 경로
    private File popFile; // 효과음 파일 경로

    private AudioInputStream audioStream; // 오디오 입력 스트림


    // 효과음 로드
    public void playEffectSound(String filePath) {
        try {
            //오디오 파일로드
            popFile = new File(filePath);
            audioStream = AudioSystem.getAudioInputStream(popFile);

            //클립생성 및 열기
            popClip = AudioSystem.getClip();
            popClip.open(audioStream);

            // 음악 재생
            popClip.start();

            // 효과음 재생 후 즉시 리소스 해제
            popClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    popClip.close();  // 효과음 종료 후 리소스 해제
                }
            });

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    // 배경음악 재생 메서드
    public void playMusicSound(String filePath) {
        try {
            //오디오 파일로드
            musicFile = new File(filePath);
            audioStream = AudioSystem.getAudioInputStream(musicFile);

            //클립생성 및 열기
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);

            // 음악 재생
            musicClip.start();
            System.out.println("배경음악 재생");

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();

        }
    }
    // 배경음악 중지 메서드
    public void stopMusic() {
        if (musicClip.isRunning()) {
            musicClip.stop();
            System.out.println("배경음악 중지");
        }
    }
}
