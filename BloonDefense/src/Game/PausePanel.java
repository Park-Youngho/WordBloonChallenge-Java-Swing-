package Game;
import main.IntroPanel;
import music.MusicPlayer;
import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel{
    private ButtonPanel buttonPanel; //버튼패널 정의

    public PausePanel(Runnable resumeGame, Runnable onMainMenu){

        setLayout(new BorderLayout()); //PausePanel 레이아웃 설정
        setOpaque(false); // 투명도 설정
        setBackground(new Color(10, 117, 216, 150));
        setPauseLabel(); //GamePause를 나타내는 글씨 레이블

        buttonPanel = new ButtonPanel(resumeGame, onMainMenu); //버튼 생성 후 게임재개와 메인메뉴로 돌아가는 함수 인자로 보내기

        add(buttonPanel, BorderLayout.CENTER); //버튼 가운데 고정
    }
    public void setPauseLabel(){
        JLabel gamePauseLabel = new JLabel("Game Pause", SwingConstants.CENTER); //레이블 정의
        gamePauseLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32)); //글씨체 설정
        gamePauseLabel.setForeground(Color.WHITE); //글씨체 색깔 설정
        add(gamePauseLabel, BorderLayout.NORTH); //패널 상단에 고정
    }

    class ButtonPanel extends JPanel{
        private ImageIcon mainMenuImg = new ImageIcon(getClass().getResource("/images/mainmenuButton.png")); //메인메뉴버튼 이미지
        private ImageIcon resumeButtonImg = new ImageIcon(getClass().getResource("/images/resumeButton.png")); //재개버튼 이미지
        private JButton resumeButton; //재개버튼
        private JButton mainMenuButton; //메인메뉴 버튼
        private Runnable onMainMenu; //메인메뉴로 돌아가는 함수
        private Runnable resumeGame; // 게임재개 함수
        public ButtonPanel(Runnable resumeGame, Runnable onMainMenu){
            this.resumeGame = resumeGame; // 게임재개 함수
            this.onMainMenu = onMainMenu; //메인메뉴로 돌아가는 함수
            setLayout(new GridLayout(1,2, 50,0)); //레이아웃 설정
            setBorder(BorderFactory.createEmptyBorder(290, 170, 290, 200)); //버튼두께 설정

            setButton(resumeButton, "resume"); //재개버튼 생성
            setButton(mainMenuButton, "mainMenu"); //메인메뉴로 돌아가기 버튼 생성

            setOpaque(false); //투명도 설정

        }
        public void setButton(JButton button, String type){
            //재개버튼 설정
            if(type.equals("resume")){
                button = new JButton(resumeButtonImg);
                add(button);
                button.addActionListener(e->{
                    resumeGame.run();
                    System.out.println("재개버튼 클릭");
                });
            }
            //메인메뉴버튼 설정
            else if(type.equals("mainMenu")){
                button = new JButton(mainMenuImg);
                add(button);
                button.addActionListener(e->{
                    onMainMenu.run();
                    checkMusic();
                    System.out.println("메인메뉴버튼 클릭");
                });
            }

        }

        //음악확인
        public void checkMusic(){
            if(IntroPanel.musicPlayer == null && !IntroPanel.mute){
                IntroPanel.musicPlayer = new MusicPlayer();
                IntroPanel.musicPlayer.playMusicSound("src/music/maintheme.wav");
            }
        }
    }

}
