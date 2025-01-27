package main;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RankPanel extends JPanel{
    private DefaultListModel<String> rankListModel; // 랭크목록 모델
    public JDialog rankDialog;
    public RankPanel(){
        rankDialog = new JDialog((Frame)null, "랭킹목록", true); // 랭킹 목록 다이얼로그 생성
        rankDialog.setSize(400, 400);
        rankDialog.setLayout(new BorderLayout()); // 레이아웃을 BorderLayout으로 설정

        // 랭킹 데이터를 표시할 리스트 모델과 JList 생성
        rankListModel = new DefaultListModel<>();
        JList<String> rankList = new JList<>(rankListModel);
        loadScoreFromFile("src/rank.txt");
        JScrollPane scrollPane = new JScrollPane(rankList);

        // 다이얼로그를 화면 중앙에 표시
        rankDialog.add(scrollPane, BorderLayout.CENTER);
        rankDialog.setLocationRelativeTo(null);
        rankDialog.setVisible(false);

    }
    private void loadScoreFromFile(String fileName){
        File file = new File(fileName); // 파일 객체 생성
        // 파일이 존재하지 않으면 메서드 종료
        if(!file.exists()){
            return;
        }


        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            // 파일을 한 줄씩 읽어서 리스트 모델에 추가
            while((line = reader.readLine()) != null){
                rankListModel.addElement(line.trim()); // 불러온 문자열의 공백 제거 후 추가
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
