package main;

import javax.swing.*;
import java.awt.*;
import Game.GameScreenPanel;
import music.MusicPlayer;
public class IntroPanel extends JPanel {
    private MainFrame mainFrame;
    private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/images/introImage.png")); //배경이미지
    private ImageIcon startButtonIcon = new ImageIcon(getClass().getResource("/images/startButton.png")); //시작버튼이미지
    private ImageIcon rankButtonIcon = new ImageIcon(getClass().getResource("/images/rankButton.png")); //랭킹버튼이미지
    private ImageIcon addWordsButtonIcon = new ImageIcon(getClass().getResource("/images/addWordButton.png")); //단어버튼이미지
    private ImageIcon muteIcon = new ImageIcon(getClass().getResource("/images/muteImg.png"));

    private Image backgroundImg = backgroundIcon.getImage(); 

    private JButton startButton = new JButton(startButtonIcon); //시작버튼 생성
    private JButton addWordsButton = new JButton(addWordsButtonIcon); //단어버튼 생성
    private JButton rankButton = new JButton(rankButtonIcon); // 랭킹버튼 생성
    private JButton muteButton = new JButton(muteIcon);

    private DifficultyPanel difficultyPanel; //난이도 패널
    private GameScreenPanel gameScreenPanel; //게임화면패널
    private AddWordsPanel addWordsPanel; //단어추가패널
    private RankPanel rankPanel; //랭킹패널


    public static MusicPlayer musicPlayer; //퍼즈패널에서 음악을 제어하기 위해 static으로 선언
    public static boolean mute = false;
    public IntroPanel(MainFrame mainFrame, GameScreenPanel gameScreenPanel){
        this.gameScreenPanel = gameScreenPanel;
        this.mainFrame = mainFrame;
        setLayout(null);

        //시작버튼
        activateButton(startButton, 250, 483, 380, 90, "start");

        //단어추가버튼
        activateButton(addWordsButton, 250, 580, 380, 90, "addWord");

        //랭크버튼
        activateButton(rankButton, 250, 670, 377, 90, "rank");

        activateMuteButton(muteButton, 30, 30, 70, 70);

        //난이도 패널
        difficultyPanel = new DifficultyPanel();
        difficultyPanel.setVisible(false);
        add(difficultyPanel);

        //단어추가패널
        addWordsPanel = new AddWordsPanel();
        addWordsPanel.setVisible(false);



        //배경음악
        musicPlayer = new MusicPlayer();
        musicPlayer.playMusicSound("src/music/maintheme.wav");
    }

    public void activateMuteButton(JButton muteButton, int x, int y, int width, int height){
        muteButton.setBounds(x,y,width,height);
        muteButton.addActionListener(e->{
            System.out.println("음소거버튼 클릭");
            if(musicPlayer != null){
                musicPlayer.stopMusic();
                musicPlayer = null;
                mute = true;
            }
            else if(musicPlayer == null){
                musicPlayer = new MusicPlayer();
                musicPlayer.playMusicSound("src/music/maintheme.wav");
                mute = false;
            }
        });
        add(muteButton);
    }

    public void activateButton(JButton button, int x, int y, int width, int height, String type){
        button.setBounds(x, y,width,height);
        decorateButton(button);
        if(type.equals("start")){
            button.addActionListener(e -> {
                System.out.println("시작버튼 클릭");
                showDifficultyPanel();
            });
        }
        else if(type.equals("addWord")){
            button.setBounds(x,y,width,height);
            button.addActionListener(e ->{
                System.out.println("단어추가 버튼 클릭");
                showAddWordsPanel();
            });
        }
        else if(type.equals("rank")){
            button.setBounds(250, 670, 377, 90);
            button.addActionListener(e ->{
                System.out.println("랭킹버튼 클릭");
                showRankPanel();
            });
        }
        add(button);
    }
    public void decorateButton(JButton button){

        button.setContentAreaFilled(false); // 배경 제거
        button.setBorderPainted(false);     // 테두리 제거
        button.setFocusPainted(false);      // 포커스 테두리 제거
        button.setOpaque(false);            // 투명 설정
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 이미지 그리기
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null);


    }

    public void showDifficultyPanel(){
        difficultyPanel.setBounds(200, 300, 500, 164); // 위치와 크기 지정
        difficultyPanel.setVisible(true);

        setComponentZOrder(difficultyPanel, 0);
        revalidate();
        repaint();
        System.out.println("LoginPanel이 표시됩니다.");


    }
    public void showAddWordsPanel(){
        addWordsPanel.wordDialog.setVisible(true);

        System.out.println("단어추가 Panel이 표시됩니다.");
    }
    public void showRankPanel(){
        //랭킹패널
        if(rankPanel != null){
            remove(rankPanel);
            rankPanel = null;
        }
        rankPanel = new RankPanel();
        rankPanel.rankDialog.setVisible(true);
        System.out.println("랭크 Panel이 표시됩니다.");
    }

    class DifficultyPanel extends JPanel{


        private ImageIcon easyButtonIcon = new ImageIcon(getClass().getResource("/images/easyButton.png"));
        private ImageIcon normalButtonIcon = new ImageIcon(getClass().getResource("/images/normalButton.png"));
        private ImageIcon hardButtonIcon = new ImageIcon(getClass().getResource("/images/hardButton.png"));

        JButton easyButton;
        JButton normalButton;
        JButton hardButton;

        public static String difficult = "null"; //초기화

        public DifficultyPanel(){
            setLayout(new GridLayout(1,3,10,0));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            easyButton = createButton(easyButtonIcon, "easy");
            add(easyButton);
            normalButton = createButton(normalButtonIcon, "normal");
            add(normalButton);
            hardButton = createButton(hardButtonIcon, "hard");
            add(hardButton);
            setOpaque(false);


        }
        public JButton createButton(ImageIcon buttonIcon, String difficult) {
            JButton button = new JButton(buttonIcon);

            button.addActionListener(e -> {
                System.out.println("난이도" + difficult + " 버튼 클릭");
                gameStart(difficult);

                if(musicPlayer != null){
                    musicPlayer.stopMusic();
                    musicPlayer = null;
                }

                setVisible(false);

            });
            return button;
        }

        public void gameStart(String difficult){
            gameScreenPanel.startGame(difficult);
            mainFrame.showGamePanel();
        }
    }
}