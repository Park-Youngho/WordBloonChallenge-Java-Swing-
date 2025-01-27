package Game;

import main.IntroPanel;
import main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import music.MusicPlayer;

public class GameOverPanel extends JPanel{ //게임 오버시 등장하는 패널

    private JButton mainMenuButton; //메인메뉴 버튼
    private JTextField nickNameField; //닉네임입력 레이블
    private Runnable onMainMenu; //메인메뉴로 돌아가는 람다식
    private JPanel centerPanel;
    private JLabel nickNameLabel;
    private MusicPlayer musicPlayer; //음악재생 객체
    public GameOverPanel(Runnable onMainMenu){
        this.onMainMenu = onMainMenu;
        setLayout(new BorderLayout());
        setBackground(new Color(0,0,0,150)); //배경색 설정

        // 상단: 게임오버 표시
        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        gameOverLabel.setForeground(Color.RED);
        add(gameOverLabel, BorderLayout.NORTH);
        
        //중앙: 닉네임 입력
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // 닉네임 라벨
        setNickNameLabel();

        // 간격 추가
        centerPanel.add(Box.createVerticalStrut(30));

        // 닉네임 입력 필드
        createNickNameField();
        add(centerPanel, BorderLayout.CENTER);

        //메인메뉴로 돌아가는 확인 버튼
        createMainMenuButton();

        setVisible(false);
    }
    public void setNickNameLabel(){
        nickNameLabel = new JLabel("Enter your Nickname", SwingConstants.CENTER);
        nickNameLabel.setForeground(Color.WHITE);
        nickNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
        centerPanel.add(nickNameLabel);
    }
    public void createNickNameField(){
        nickNameField = new JTextField(10); // 입력 글자수 기준 크기
        nickNameField.setMaximumSize(new Dimension(200, 30)); // 최대 크기 설정
        nickNameField.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 중앙 정렬
        nickNameField.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
        centerPanel.add(nickNameField); //닉네임입력란설정
    }
    public void createMainMenuButton(){
        mainMenuButton = new JButton("확인");
        mainMenuButton.addActionListener(e->{
            rankScoreToFile();
            if (onMainMenu != null) {
                onMainMenu.run(); // 메인 메뉴 동작 실행
                setVisible(false);
                checkMusic();
            }
        });
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
        centerPanel.add(mainMenuButton);
    }
    public void checkMusic(){
        if(IntroPanel.musicPlayer == null && !IntroPanel.mute){
            IntroPanel.musicPlayer = new MusicPlayer();
            IntroPanel.musicPlayer.playMusicSound("src/music/maintheme.wav");
        }
    }
    private void rankScoreToFile(){
        String nickName = nickNameField.getText();
        File file = new File("src/rank.txt");
        if (nickName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "닉네임을 입력해주세요!","", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try{
            //기존 데이터 불러오기
            List<String> lines = new ArrayList<>();
            if(file.exists()){
                try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // 순위 숫자 제거 후 데이터만 저장
                        String[] split = line.split(" ", 2);
                        if (split.length > 1) {
                            lines.add(split[1]);
                        }
                    }
                }
            }
            int score = StatusPanel.getScore();
            String newLine = nickName + ":" + score;
            lines.add(newLine);

            //점수기준으로 내림차순
            lines.sort((a, b) ->{
                int scoreA = Integer.parseInt(a.split(":")[1]);
                int scoreB = Integer.parseInt(b.split(":")[1]);
                return Integer.compare(scoreB, scoreA);
            });

            // 중복 데이터 확인 및 순위 적용
            Set<String> uniqueSet = new HashSet<>();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                int rank = 1;
                for (String line : lines) {
                    if (uniqueSet.add(line)) { // 중복 데이터가 아니면 저장
                        writer.write(rank + "위 " + line);
                        writer.newLine();
                        rank++;
                    }
                }
            }
            System.out.println("랭킹 저장 성공");
        }catch(IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "파일 저장 중 오류가 발생했습니다.");
        }
    }
}
