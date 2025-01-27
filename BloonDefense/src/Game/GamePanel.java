package Game;

import javax.swing.*;
import java.awt.*;

import main.*;

public class GamePanel extends JPanel{
    private JSplitPane splitPane; //GameScreenPanel과  StatusPanel로 나누기 위한 SPlitPane
    private GameScreenPanel gameScreenPanel; 
    private StatusPanel statusPanel;


    public GamePanel(MainFrame mainFrame){

        setLayout(new BorderLayout()); //레이아웃 설정
        statusPanel = new StatusPanel(); //statusPanel 생성
        gameScreenPanel = new GameScreenPanel(mainFrame, statusPanel); //GameScreenPanel에서 메인화면 전환을 위해 인자로 mainFrame 넘기기
        setSplitPane(); //SplitPane 만드는 함수

    }
    public void setSplitPane(){ 
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gameScreenPanel, statusPanel);
        splitPane.setDividerLocation(700); //GameScreenPanel 크기 나머지는 StatusPanel
        splitPane.setEnabled(false); //크기조정 못하게 고정
        splitPane.setDividerSize(1); //나누는 선 크기 
        add(splitPane, BorderLayout.CENTER); // 게임패널에 추가
    }
    public GameScreenPanel getGameScreenPanel(){
        return gameScreenPanel;
    }

}
