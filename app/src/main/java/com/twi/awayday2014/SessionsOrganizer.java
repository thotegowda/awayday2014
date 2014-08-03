package com.twi.awayday2014;

import com.twi.awayday2014.models.Presentation;
import com.twi.awayday2014.models.Presenter;

import java.util.Arrays;
import java.util.List;

public class SessionsOrganizer {

    private final List<Presentation> presentations;
    private final List<Presentation> keynotes;

    public SessionsOrganizer() {
        keynotes = Arrays.asList(
                new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "keynote title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "keynote title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "keynote title3", "29/09/14 1.00 - 4.00")
        );

        presentations = Arrays.asList(
                new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name4", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name5", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name6", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name7", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name8", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name9", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name10", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name4", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name5", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name6", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name7", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name8", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name9", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name10", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name1", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name2", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name3", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name4", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name5", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name6", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name7", R.drawable.speaker_00), "session title1", "29/09/14 10.00 - 11.00"),
                new Presentation(new Presenter("Presenter name8", R.drawable.speaker_01), "session title2", "29/09/14 12.00 - 1.00"),
                new Presentation(new Presenter("Presenter name9", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00"),
                new Presentation(new Presenter("Presenter name10", R.drawable.speaker_02), "session title3", "29/09/14 1.00 - 4.00")
        );
    }

    public Presentation getById(long id) {
        return Presentation.findById(Presentation.class, id);
    }

    public List<Presentation> keynotes() {
        return keynotes;
    }

    public List<Presentation> sessions() {
        return presentations;
    }
}
