import java.util.*;
import java.io.*;
class MasterMind {
    private static int defaultmaxAllowTime = 8;
    private static int QUIZZ_LENGTH = 4;
    private static char allCorrect='*';
    private static char numberCorrect='!';
    public enum State {PROGRESS, LOST, WIN }
    private String hiddenNumber;
    private State gameState;
    private int maxAllowTime;
    private List<Answers> attemptList;
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
            this.attemptList.add(Ans);
        }
        changeGameStatus(Ans);
    }
    private void changeGameStatus(Answers paraAttmpt)
    {
        if (attemptList.size() < maxAllowTime)
        {
            if (matchResult(paraAttmpt.getAnswer()).equals("****"))
                gameState=State.WIN;
        }
        else
            gameState = State.LOST;
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
class Answers
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
public class MasterMindGame
{
    public static void run () throws IOException
    {
        MasterMind mGame = new MasterMind();
        while (mGame.isProgress()) {
            System.out.print("Enter Your Guess : ");
            String test = acceptNumber();
            Answers ans = new Answers(test);
            System.out.print(mGame.getResult(ans) + "\n");
            mGame.evaluateResult(ans);
            if (mGame.isWon()) {
                System.out.print("YOU WIN\n");
                System.out.print("CORRECT ANSWER IS " +
                        mGame.getHiddenNumber());
                break;
            }
        }
        if (mGame.isGameOver()) {
            System.out.print("YOU LOST \n");
            System.out.print("CORRECT ANSWER IS " +
                    mGame.getHiddenNumber());
        }
    }
    public static void displayScreen ()
    {
        System.out.println("Please Guess four Hidden Digits");
    }
    private static String acceptNumber () throws IOException
    {
        BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
    public static void main(String[] args) throws IOException {
        displayScreen();
        run();
    }
}


