import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.event.*;
public class Game_GUI {

        JFrame gFrame = new JFrame();
        JButton sBtn = new JButton();
        JButton resetBtn = new JButton();
        JLabel captionLabel = new JLabel();
        JLabel timeLabel = new JLabel();
        JLabel errorLabel = new JLabel();
        JTextField AnsText = new JTextField();
        JLabel[] resLabels = new JLabel[9];
        JLabel[] ansLabels = new JLabel[9];
        int yRes_cordinate = 210;
        int yAns_cordinate = 180;
        static int nTest = 1;
        int maxTest = 8;
        int nResLabel = 8;
        int nAnsLabel = 8;

        String test = "";
        boolean startGetText = false;

        int elapsedTime = 5*60*1000;
        int seconds = 0;
        int minutes = 5;
        int hours = 0;
        boolean start = true;
        String seconds_format = String.format("%02d", seconds);
        String minutes_format = String.format("%02d", minutes);
        String hours_format = String.format("%02d", hours);

        TimeAction timeListener = new TimeAction();
        Timer aTime = new Timer(1000, timeListener);

        Game_GUI()
        {
                timeLabel.setText(hours_format+":"+minutes_format+":"+seconds_format);
                timeLabel.setBounds(400, 300, 200, 100);
                timeLabel.setFont(new Font("Verdana",Font.PLAIN,35));
                timeLabel.setBorder(BorderFactory.createLineBorder(Color.white));
                timeLabel.setOpaque(true);
                timeLabel.setHorizontalAlignment(JTextField.CENTER);
                timeLabel.setBackground(Color.black);
                timeLabel.setForeground(Color.cyan);

                captionLabel.setText("Enter your guess ");
                captionLabel.setBounds(100,50,150,50);
                captionLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
                captionLabel.setOpaque(true);
                captionLabel.setHorizontalAlignment(JTextField.LEFT);

                errorLabel.setBounds(100,150,400,30);
                errorLabel.setVisible(false);
                errorLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
                errorLabel.setForeground(Color.red);
                errorLabel.setOpaque(true);
                errorLabel.setHorizontalAlignment(JTextField.LEFT);

                AnsText.setBounds(100, 100, 150, 30);
                AnsText.setEnabled(false);
                AnswerTextAction ansListener = new AnswerTextAction();
                AnsText.addKeyListener(ansListener);

                for(int i=0; i<nResLabel+1; i++)
                {
                        resLabels[i] = new JLabel();
                        resLabels[i].setBounds(100,yRes_cordinate + i*60,150,30);
                        resLabels[i].setVisible(false);
                        resLabels[i].setBorder(BorderFactory.createLineBorder(Color.black));
                        gFrame.add(resLabels[i]);
                }

                for(int i=0; i<nAnsLabel+1; i++)
                {
                        ansLabels[i] = new JLabel();
                        ansLabels[i].setBounds(100,yAns_cordinate + i*60,150,30);
                        ansLabels[i].setVisible(false);
                        ansLabels[i].setBorder(BorderFactory.createLineBorder(Color.red));
                        gFrame.add(ansLabels[i]);
                }

                ButtonAction buttonListener = new ButtonAction();
                sBtn.setText("START");
                sBtn.setBounds(400,400,100,70);
                sBtn.setFont(new Font("Verdana", Font.PLAIN, 20));
                sBtn.setFocusable(false);
                sBtn.setBackground(Color.red);
                sBtn.addActionListener(buttonListener);

                resetBtn.setText("RESET");
                resetBtn.setBounds(500,400,100,70);
                resetBtn.setFont(new Font("Verdana", Font.PLAIN, 20));
                resetBtn.setFocusable(false);
                resetBtn.setBackground(Color.green);
                resetBtn.addActionListener(buttonListener);
                
                gFrame.add(timeLabel);
                gFrame.add(sBtn);
                gFrame.add(resetBtn);
                gFrame.add(captionLabel);
                gFrame.add(AnsText);
                gFrame.add(errorLabel);
                gFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gFrame.setSize(700, 900);
                gFrame.setLayout(null);
                gFrame.setVisible(true);

        }
        public boolean checkPara(String para)
        {
                if(para.length() != 4)
                {
                        errorLabel.setText("input length must be 4 characters");
                        return false;
                }
                else if(para.length() == 4)
                {
                        for(int i=0;i<para.length();i++)
                        {
                                if(para.charAt(i) < '0' || para.charAt(i) > '9')
                                {
                                        errorLabel.setText("Input must be numbers");
                                        return false;
                                }
                        }
                }
                return true;
        }
        public void MainGame()
        {
                MasterMind mGame = new MasterMind();
                System.out.println(nTest);
                if (nTest <= maxTest) 
                {
                        if(startGetText)
                        {
                                Answers ans = new Answers(test);
                                mGame.evaluateResult(ans);
                                resLabels[nTest-1].setText("Answer is: " + mGame.getResult(ans));
                                resLabels[nTest-1].setVisible(true);
                                ansLabels[nTest-1].setText("Your answer is: " + test);
                                ansLabels[nTest-1].setVisible(true);
                                if (mGame.isWon()) 
                                {
                                        resLabels[nResLabel].setBounds(100,yRes_cordinate + 16*30,150,60);
                                        resLabels[nResLabel].setText("<html>YOU WIN <br> CORRECT ANSWER IS " 
                                        + mGame.getHiddenNumber() + "</html>");
                                        resLabels[nResLabel].setVisible(true);
                                        AnsText.setEnabled(false);
                                        aTime.stop();
                                        sBtn.setText("END");
                                        AnsText.setText("");
                                        sBtn.setEnabled(false);
                                        sBtn.setBackground(Color.red);
                                        start = true;
                                }
                                else if(mGame.isGameOver())
                                {
                                        resLabels[nResLabel].setBounds(100,yRes_cordinate + 16*30,150,60);
                                        resLabels[nResLabel].setText("<html>YOU LOST <br> CORRECT ANSWER IS " + mGame.getHiddenNumber() + "</html>");
                                        resLabels[nResLabel].setVisible(true);
                                        AnsText.setEnabled(false);
                                        aTime.stop();
                                        sBtn.setText("END");
                                        AnsText.setText("");
                                        sBtn.setEnabled(false);
                                        sBtn.setBackground(Color.red);
                                        start = true;
                                }
                        }
                        nTest++;
                }
        }

        class TimeAction implements ActionListener
        {
                public void actionPerformed(ActionEvent e)
                {
                        elapsedTime -= 1000;
                        hours = elapsedTime/3600000;
                        minutes = (elapsedTime/60000)%60;
                        seconds = (elapsedTime/1000)%60;

                        seconds_format = String.format("%02d", seconds);
                        minutes_format = String.format("%02d", minutes);
                        hours_format = String.format("%02d", hours);

                        timeLabel.setText(hours_format+":"+minutes_format+":"+seconds_format);

                }
        }
        class ButtonAction implements ActionListener
        {
                public void actionPerformed(ActionEvent e)
                {
                        if(e.getSource() == sBtn && start)
                        {
                                aTime.start();
                                sBtn.setText("STOP");
                                sBtn.setEnabled(true);
                                sBtn.setBackground(Color.yellow);
                                AnsText.setEnabled(true);
                                start = false;
                        }
                        else if(e.getSource() == sBtn)
                        {
                                aTime.stop();
                                sBtn.setText("START");
                                sBtn.setEnabled(true);
                                sBtn.setBackground(Color.red);
                                AnsText.setEnabled(false);
                                start = true;
                        }
                        else if(e.getSource() == resetBtn)
                        {
                                aTime.stop();
                                sBtn.setText("START");
                                sBtn.setBackground(Color.red);
                                elapsedTime = 5*60*1000;
                                seconds = 0;
                                minutes = 5;
                                hours = 0;
                                seconds_format = String.format("%02d", seconds);
                                minutes_format = String.format("%02d", minutes);
                                hours_format = String.format("%02d", hours);
                                timeLabel.setText(hours_format+":"+minutes_format+":"+seconds_format);
                                AnsText.setEnabled(false);
                                sBtn.setEnabled(true);
                                for(int i=0; i<nResLabel+1; i++)
                                {
                                        resLabels[i].setVisible(false);
                                        ansLabels[i].setVisible(false);
                                }
                                // Restart test number
                                nTest = 1;
                                start = true;
                        }
                }
        }
        class AnswerTextAction implements KeyListener
        {
                public void keyTyped(KeyEvent e) {
                        //TODO
                }
            
                public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                test = AnsText.getText();
                                if(checkPara(test))
                                {
                                        startGetText = true;
                                        MainGame();
                                        errorLabel.setVisible(false);
                                }
                                else 
                                {
                                        errorLabel.setVisible(true);
                                }
                        }
                        //TODO
                }
            
                public void keyReleased(KeyEvent e) {
                        //TODO
                }
        }
        static class MasterMind 
        {
                private static int defaultmaxAllowTime = 8;
                private static int QUIZZ_LENGTH = 4;
                private static char allCorrect='*';
                private static char numberCorrect='!';
                public static enum State {PROGRESS, LOST, WIN }
                private String hiddenNumber;
                private static State gameState = State.PROGRESS;
                private int maxAllowTime;
                private static List<Answers> attemptList;
                private String correctAnswer = new String();
                public MasterMind()
                {
                        gameState = State.PROGRESS;
                        hiddenNumber = generateHiddenNumber();
                        maxAllowTime = defaultmaxAllowTime;
                        for(int i=1;i<= QUIZZ_LENGTH;i++)
                        {
                        correctAnswer = correctAnswer + allCorrect;
                        }
                        attemptList = new ArrayList<Answers>();
                }
                public String getHiddenNumber()
                {
                        return hiddenNumber;
                }
                public boolean isProgress()
                {
                        return gameState==State.PROGRESS;
                }
                public boolean isGameOver()
                {
                        return gameState==State.LOST;
                }
                public boolean isWon()
                {
                        return gameState==State.WIN;
                }
                private String generateHiddenNumber()
                {
                        Random rand = new Random();
                        return String.format("%04d", rand.nextInt(10000));
                }
                public String getResult(Answers ans)
                {
                        ans.setResult(matchResult(ans.getAnswer()));
                        return ans.getResult();
                }
                public void evaluateResult(Answers Ans)
                {
                        if (isProgress())
                        {
                                Ans.setResult(matchResult(Ans.getAnswer()));
                                // this.attemptList.add(Ans);
                        }
                        changeGameStatus(Ans);
                }
                private void changeGameStatus(Answers paraAttmpt)
                {
                        if (nTest < defaultmaxAllowTime)
                        {
                                if (matchResult(paraAttmpt.getAnswer()).equals("****"))
                                {
                                        gameState=State.WIN;
                                }
                        }
                        else
                        {
                                if (matchResult(paraAttmpt.getAnswer()).equals("****"))
                                {
                                        gameState=State.WIN;
                                }
                                else 
                                {
                                        gameState = State.LOST;
                                }
                        }
                }
                public String matchResult(String inputNumber)
                {
                        char[] inChar;
                        char[] hidChar;
                        String rtnValue = new String();
                        inChar = inputNumber.toCharArray();
                        hidChar = hiddenNumber.toCharArray();
                        for (int i=0;i < hiddenNumber.length();i++)
                        {
                        if (inChar[i]==hidChar[i])
                        {
                                //mark the character is already used
                                inChar[i] = '#';
                                rtnValue= rtnValue + this.allCorrect;
                        }
                        else
                        {
                                for (int j=0;j < hiddenNumber.length();j++)
                                {
                                if ( inChar[i] == hidChar[j] )
                                {
                                        //mark the character is already used
                                        inChar[i] = '#';
                                        rtnValue=rtnValue + this.numberCorrect;
                                        break;
                                }
                                }
                        }
                        }
                        return rtnValue;
                }
        }
        static class Answers
        {
                private String answer;
                private String result;
                public Answers(){};
                public Answers(String ans)
                {
                        this.answer = new String(ans);
                        result= new String();
                }
                public String getResult(){return this.result;}
                public String getAnswer(){return this.answer;}
                public void setAnswer(String answer){this.answer = answer;}
                public void setResult(String result){this.result = result;}
        }

        public static void main(String[] args)
        {
                Game_GUI test = new Game_GUI();
        }
}
