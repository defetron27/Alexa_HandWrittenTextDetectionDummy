import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Dummy
{
    public static void main(String[] args)
    {
        detectDocumentText();
    }

    private static void detectDocumentText()
    {
        try
        {
            String imageUrl = "https://s3.amazonaws.com/solverpro/a.jpg";

            URL url = new URL(imageUrl);

            InputStream in = new BufferedInputStream(url.openStream());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];

            int n;

            while (-1!=(n=in.read(buf)))
            {
                byteArrayOutputStream.write(buf, 0, n);
            }

            byteArrayOutputStream.close();
            in.close();

            byte[] responseBytes = byteArrayOutputStream.toByteArray();

            PrintStream out = System.out;

            List<AnnotateImageRequest> requests = new ArrayList<>();

            ByteString imgBytes = ByteString.copyFrom(responseBytes);

            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            try (ImageAnnotatorClient client = ImageAnnotatorClient.create())
            {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();
                client.close();

                for (AnnotateImageResponse res : responses)
                {
                    if (res.hasError())
                    {
                        out.printf("Error: %s\n", res.getError().getMessage());
                        return;
                    }

                    TextAnnotation annotation = res.getFullTextAnnotation();

                    out.println(annotation.getText());
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
