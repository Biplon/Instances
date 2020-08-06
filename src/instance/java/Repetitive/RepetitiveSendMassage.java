package instance.java.Repetitive;

import instance.java.Enum.RepetitiveType;

public class RepetitiveSendMassage extends Repetitive
{
    private final String text;

    public String getText()
    {
        return text;
    }

    public RepetitiveSendMassage(RepetitiveType type, int timer,String text)
    {
        super(type, timer);
        this.text = text;
    }
}
