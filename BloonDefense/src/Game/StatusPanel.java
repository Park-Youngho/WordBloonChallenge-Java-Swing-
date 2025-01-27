package Game;

import Game.GameScreenPanel;
import Game.PausePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

//상태창
public class StatusPanel extends JPanel {
    private ImageIcon statusImage = new ImageIcon(getClass().getResource("../images/statusImage.png")); //상태창 이미지
    private ImageIcon lifeImage= new ImageIcon(getClass().getResource("../images/hearts.png")); //생명 이미지
    private JLabel scoreLabel; //점수판
    private static int score = 0;
    
    public ArrayList<Image> lifes = new ArrayList<>(); //생명이미지를 담을 배열생성


    public StatusPanel(){

        setLayout(new BorderLayout()); //레이아웃 설정

        setScoreLabel(); //점수판 설정
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER); //중앙 정렬
        add(scoreLabel, BorderLayout.NORTH); //점수판 상단에 고정


        //생명
        for(int i=0; i<5; i++) {
            lifes.add(lifeImage.getImage()); //라이프 이미지 붙이기
        }
    }

    public void setScoreLabel(){
        scoreLabel = new JLabel("SCORE " + score); //점수판 생성
        scoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18)); //점수 글씨체 설정
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT); //중앙정렬
        scoreLabel.setForeground(Color.GREEN); //글씨색깔설정
    }
    public void increaseScore(){
        score += 10; //10점씩 상승
        System.out.println("점수증가 현재 점수: " + score);
        scoreLabel.setText("SCORE" + score); //텍스트 업데이트
    }
    public void decreaseLife(){
        if(lifes.size() > 0){
            lifes.remove(lifes.get(lifes.size()-1)); //생명 이미지 1개씩 감소
            repaint(); //이미지 업데이트
            System.out.println("생명 -1");
        }
    }
    public static int getScore(){
        return score; //점수 리턴
    }
    public void init(){ //상태창 초기화
        score =0; //점수 0으로 초기화
        scoreLabel.setText("SCORE" + score); //텍스트 업데이트

        //생명초기화
        lifes.clear();
        for(int i=0; i<5; i++) {
            lifes.add(lifeImage.getImage());
        }

    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(statusImage.getImage(), 0,0, getWidth(), getHeight(), null);

        int x = 23;
        int y = 50;
        for(int i=0; i<lifes.size(); i++){
            g.drawImage(lifeImage.getImage(), x, y, 30, 30, null);
            x+=30;
        }

    }

}