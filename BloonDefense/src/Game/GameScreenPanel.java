package Game;


import main.MainFrame;
import music.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//게임패널
public class GameScreenPanel extends JPanel {
    private JTextField textField;
    private final List<Bloon> bloons = new CopyOnWriteArrayList<>();
    private ImageIcon backgroundImage = new ImageIcon(getClass().getResource("../images/background.jpg"));
    private StatusPanel statusPanel;
    private GameOverPanel gameOverPanel;
    private GameThread gameThread;
    private ArrayList<String> wordList = new ArrayList<>();
    private MainFrame mainFrame;
    private MusicPlayer musicPlayer;
    private PausePanel pausePanel;
    private ImageIcon pauseImage = new ImageIcon(getClass().getResource("../images/pauseButton.png"));
    private JButton pauseButton;

    public GameScreenPanel(MainFrame mainFrame, StatusPanel statusPanel){
        this.mainFrame = mainFrame;
        this.statusPanel = statusPanel;
        setLayout(new BorderLayout());
        setTextField(); //텍스트필드 설정
        loadWords(); //단어불러오기
        musicPlayer = new MusicPlayer();
        setPauseButton(); //퍼즈버튼 설정
    }

    public void startGame(String difficult) {
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new GameThread(difficult);
            gameThread.setName("GameThread");
            gameThread.start();

            System.out.println("GameThread가 시작되었습니다.");
        } else {
            System.out.println("GameThread는 이미 실행 중입니다.");
        }
    }


    public void gameOver(){
        if (gameThread != null) {
            gameThread.stopGame(); // 게임 스레드 중지
            musicPlayer.playEffectSound("src/music/gameOverSound.wav");
            System.out.println("게임오버");
        }
        if(gameOverPanel == null){
            gameOverPanel = new GameOverPanel(this::goToMainMenu);
            gameOverPanel.setOpaque(true); // 패널 불투명
            gameOverPanel.setSize(getSize()); // 패널 크기 설정
            gameOverPanel.setVisible(true);

            // 레이아웃 설정: JLayeredPane 활용
            JLayeredPane layeredPane = getRootPane().getLayeredPane();
            layeredPane.add(gameOverPanel, JLayeredPane.POPUP_LAYER);
            gameOverPanel.setLocation(0, 0);
        }

    }
    public void gamePause(){
        if(gameThread != null){

            gameThread.pauseGame();
            System.out.println("스레드 일시정지");
        }
        if(pausePanel == null){
            
            pausePanel = new PausePanel(this::resumeGame, this::goToMainMenu);
            pausePanel.setOpaque(true);
            pausePanel.setSize(getSize());

            // 레이아웃 설정: JLayeredPane 활용
            JLayeredPane layeredPane = getRootPane().getLayeredPane();
            layeredPane.add(pausePanel, JLayeredPane.POPUP_LAYER);
            pausePanel.setLocation(0, 0);
        }
        pausePanel.setVisible(true);
        revalidate();
        repaint();

    }
    public void setPauseButton(){
        //퍼즈버튼
        pauseButton = new JButton(pauseImage);
        pauseButton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        

        //퍼즈버튼을 누르면 스레드 중지
        pauseButton.addActionListener(e->{
            System.out.println("pause버튼 클릭");
            gamePause();
        });
    }
    public void initGame(){
        gameThread.paused = false;
        System.out.println("게임초기화");
        // 풍선 리스트 초기화
        bloons.clear();

        // 점수 및 생명 초기화
        if (statusPanel != null) {
            System.out.println("생명초기화");
            statusPanel.init(); // 점수와 생명을 초기화하는 메서드
        }
        // 텍스트 필드 초기화
        if (textField != null) {
            textField.setText(""); // 사용자 입력 초기화
        }

        // 기존 스레드 종료
        if (gameThread != null) {
            gameThread.stopGame(); // 기존 스레드 중지
        }
    }
    public void resumeGame(){
        if(gameThread != null){
            gameThread.getName();
            gameThread.resumeGame();
            pausePanel.setVisible(false);
            remove(pausePanel);
            pausePanel = null;
        }

    }
    public void goToMainMenu(){
        if(gameOverPanel != null){
            remove(gameOverPanel);
            gameOverPanel = null;
        }
        if(pausePanel != null){
            pausePanel.setVisible(false);
            remove(pausePanel);
            pausePanel = null;

        }
        mainFrame.showIntroPanel();
        initGame();

    }

    public void setTextField(){

        JPanel textFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        textFieldPanel.setOpaque(false); // 배경 투명
        textField = new JTextField(30);
        textField.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText();
                textField.setText("");

                Iterator<Bloon> iterator = bloons.iterator();
                while(iterator.hasNext()){
                    Bloon bloon = iterator.next();
                    
                    //풍선이 텍스트필드에 입력한 글자와 맞다면 제거 후 사운드 출력
                    if(bloon.getWord().equals(input)){ 
                        statusPanel.increaseScore();
                        bloons.remove(bloon);
                        musicPlayer.playEffectSound("src/music/bloonPop.wav");
                        break;
                    }
                }

            }
        });

        textFieldPanel.add(textField);

        setPauseButton();
        textFieldPanel.add(pauseButton);
        this.add(textFieldPanel, BorderLayout.SOUTH);
    }
    public void updateBloon(){
        for (Bloon bloon : bloons) {
            bloon.move();
            if (bloon.outOfScreen()) {
                bloons.remove(bloon); // 화면 밖으로 나간 풍선 제거
                statusPanel.decreaseLife(); //화면 밖으로 나가면 생명감소
                if(statusPanel.lifes.size() == 0){
                    gameOver();
                }

            }
        }
        repaint();
    }
    public void spawnBloon(String difficult){

        int x = (int)(Math.random()*(getWidth() -50));
        int y = getHeight();
        int speed = 0;
        if(difficult.equals("easy")) speed = (int)(Math.random()*1)+1;
        else if(difficult.equals("normal")) speed = (int)(Math.random()*5)+2;
        else if(difficult.equals("hard")) speed = (int)(Math.random()*10)+5;

        String word = wordList.get((int)(Math.random()*wordList.size()));
        System.out.println("풍선 생성: x=" + x + ", y=" + y + ", 단어=" + word);

        ImageIcon bloonImage = new ImageIcon(getClass().getResource("../images/bloonImg.png"));
        Image bloon = bloonImage.getImage().getScaledInstance(40, 80, Image.SCALE_SMOOTH);

        Bloon newBloon = new Bloon(word, bloon, x, y, speed);
        bloons.add(newBloon);
    }

    private void loadWords(){
        try(BufferedReader reader = new BufferedReader(new FileReader(getClass().getResource("/words.txt").getPath()))){
            String line;
            while((line = reader.readLine()) != null){
                wordList.add(line.trim());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(backgroundImage.getImage(), 0,0, getWidth(), getHeight(), null);

        for(Bloon bloon : bloons){
            g.drawImage(bloon.getBloonImage(), bloon.getX(), bloon.getY(), null);

            // 단어 표시
            g.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            g.setColor(Color.BLACK);
            g.drawString(bloon.getWord(), bloon.getX(), bloon.getY() + 90);
        }

    }

    
    class GameThread extends Thread {
        private boolean running = true; //실행확인 플래그
        private boolean paused = false; //중단 확인플래그
        private String difficult; //난이도 체크

        public GameThread(String difficult){
            this.difficult = difficult;
        }
        @Override
        public void run() {
            long lastSpawnTime = System.currentTimeMillis(); // 마지막 풍선 생성 시간
            long spawnInterval = 3000; // 풍선 생성 간격 (3초)

            while (running) {
                synchronized (this){
                    while(paused){
                        try{
                            wait();
                        }catch (InterruptedException e){
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                long currentTime = System.currentTimeMillis();

                // 주기적으로 풍선 생성
                if (currentTime - lastSpawnTime >= spawnInterval) {
                    spawnBloon(difficult);
                    lastSpawnTime = currentTime;
                }

                // 풍선 위치 업데이트
                updateBloon();

                // 화면 갱신
                SwingUtilities.invokeLater(GameScreenPanel.this::repaint);
                try {
                    Thread.sleep(30); // 게임 루프 간격 (30ms)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        public synchronized void stopGame() {
            System.out.println("스레드중지");
            running = false;
            interrupt(); // 스레드 정지
        }
        public synchronized void pauseGame(){
            paused = true;
        }
        public synchronized void resumeGame(){
            paused = false;
            System.out.println("스레드재개");
            notifyAll(); //스레드 재개   
        }

    }



}