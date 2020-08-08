package instance.java.Repetitive;

import instance.java.Enum.ERepetitiveType;

public class RepetitiveSendMassage extends Repetitive
{
    private final String text;

    private final boolean actionbar;

    public boolean isActionbar()
    {
        return actionbar;
    }

    public String getText()
    {
        return text;
    }

    public RepetitiveSendMassage(ERepetitiveType type, int timer, String text, boolean actionbar)
    {
        super(type, timer);
        this.text = text;
        this.actionbar = actionbar;
    }
}
