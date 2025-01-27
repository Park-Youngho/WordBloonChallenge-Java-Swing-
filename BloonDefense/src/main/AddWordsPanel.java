package main;

import Game.StatusPanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class AddWordsPanel extends JPanel{

    private DefaultListModel<String> wordListModel; // 단어 목록을 관리하는 모델
    public JDialog wordDialog; // 단어 추가를 위한 대화상자(Dialog)
    private JButton addButton; // 단어 추가 버튼
    private JTextField newWordField; // 새 단어를 입력하는 필드
    private JPanel addWordsPanel; // 단어 추가 패널
    private JScrollPane scrollPane; // 단어 목록을 스크롤 가능한 형태로 표시

    public AddWordsPanel(){

        wordDialog = new JDialog((Frame)null, "단어 추가", true);
        wordDialog.setSize(400, 400);
        wordDialog.setLayout(new BorderLayout());

        //단어 목록 표시
        wordListModel = new DefaultListModel<>();
        JList<String> wordList = new JList<>(wordListModel);
        loadWordsFromFile("src/words.txt"); //파일에서 단어읽기
        scrollPane = new JScrollPane(wordList); // 스크롤 패널로 단어 목록 추가

        //단어추가 패널
        addWordsPanel = new JPanel();
        addWordsPanel.setLayout(new FlowLayout());

        // 입력 필드 및 버튼 추가
        newWordField = new JTextField(10); // 입력 필드 길이 설정
        addButton = new JButton("추가");
        addWordsPanel.add(newWordField); // 입력 필드 패널에 추가
        addWordsPanel.add(addButton); // 버튼 패널에 추가
        
        //단어추가 버튼 이벤트 활성화
        activateAddButton();

        //다이어로그 구성
        setWordDialog();

    }
    public void setWordDialog(){
        wordDialog.add(scrollPane, BorderLayout.CENTER);
        wordDialog.add(addWordsPanel, BorderLayout.SOUTH);
        wordDialog.setLocationRelativeTo(null); // 화면 중앙에 위치
        wordDialog.setVisible(false);
    }
    public void activateAddButton(){
        addButton.addActionListener(e->{
            String newWord = newWordField.getText().trim();  // 입력값에서 앞뒤 공백 제거

            // 입력값이 비어있지 않고 중복되지 않을 경우 처리
            if(!newWord.isEmpty() && !wordListModel.contains(newWord)){
                wordListModel.addElement(newWord); //목록에 추가
                saveWordToFile("src/words.txt", newWord); //목록에 저장
                newWordField.setText(""); //입력필드 초기화
                System.out.println(newWord + " 단어가 추가되었습니다.");
            }else{
                JOptionPane.showMessageDialog(wordDialog, "단어가 비어있거나 이미 존재합니다.",  "오류", JOptionPane.ERROR_MESSAGE);

            }

        });
    }
    private void loadWordsFromFile(String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            return;  // 파일이 존재하지 않으면 반환
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // 파일의 각 줄을 읽어 단어 목록에 추가
            while ((line = reader.readLine()) != null) {
                wordListModel.addElement(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveWordToFile(String fileName, String word){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))){
            writer.write(word); // 단어 저장
            writer.newLine();// 줄바꿈
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
