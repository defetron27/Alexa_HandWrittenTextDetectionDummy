import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public class WrittenTextRequestHandler extends SpeechletRequestStreamHandler
{
    private static final Set<String> supportedApplicationIds;

    static
    {
        supportedApplicationIds = new HashSet<>();
        supportedApplicationIds.add("amzn1.ask.skill.8bf005f1-9627-4e74-b35f-c751f4f9c70f");
    }

    public WrittenTextRequestHandler()
    {
        super(new WrittenTextSpeechLet(), supportedApplicationIds);
    }
}
