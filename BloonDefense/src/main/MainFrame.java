package main;
import Game.GamePanel;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame{

    private Image icon = new ImageIcon(getClass().getResource("/images/bloonImg.png")).getImage(); //게임 아이콘
    private IntroPanel introPanel; //인트로화면
    private GamePanel gamePanel; //게임패널
    private CardLayout cardLayout = new CardLayout(); //화면전환레이아웃

    public MainFrame() {
        setTitle("Word Bollon Challenge"); //게임 제목
        setSize(900, 800); //게임프레임사이즈

        setLocationRelativeTo(null); //null을 넣어 중앙에 위치하도록 함
        setResizable(false); //화면크기변환X
        setIconImage(icon); //게임아이콘 설정

        getContentPane().setLayout(cardLayout); //화면전환레이아웃 설정
        gamePanel = new GamePanel(this); //게임화면에서 메인화면으로 화면전환을 위해 인자로 넘기기
        getContentPane().add(gamePanel, "GamePanel"); //게임패널 메인프레임에 붙이기
        introPanel = new IntroPanel(this, gamePanel.getGameScreenPanel()); //인트로패널 생성 후 메인프레임 화면전환 함수 사용을 위해 인자로 넘김
        getContentPane().add(introPanel, "IntroPanel"); //인트로패널 메인프레임에 붙이기
        cardLayout.show(getContentPane(), "IntroPanel"); //인트로패널 바로 보여주기

        setDefaultCloseOperation(EXIT_ON_CLOSE); //정상종료
        setVisible(true); //메인프레임 보여주기

    }

    public void showGamePanel(){ //게임패널 화면전환
        cardLayout.show(getContentPane(), "GamePanel");
    }
    public void showIntroPanel(){ //인트로패널 화면전환
        cardLayout.show(getContentPane(), "IntroPanel");
    }

    public GamePanel getGamePanel(){
        return gamePanel;
    }



}
