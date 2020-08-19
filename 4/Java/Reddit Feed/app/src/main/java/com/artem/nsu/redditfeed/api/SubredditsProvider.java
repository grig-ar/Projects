package com.artem.nsu.redditfeed.api;

import com.artem.nsu.redditfeed.model.ISubreddit;
import com.artem.nsu.redditfeed.db.entity.Subreddit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubredditsProvider {

    private static final ISubreddit[] SUBREDDITS = new Subreddit[]{
            new Subreddit("0x0", "/r/popular/", null),
            new Subreddit("t5_2qh1i", "/r/AskReddit/", "https://b.thumbs.redditmedia.com/EndDxMGB-FTZ2MGtjepQ06cQEkZw_YQAsOUudpb9nSQ.png"),
            new Subreddit("t5_2qh0u", "/r/pics/", "https://b.thumbs.redditmedia.com/VZX_KQLnI1DPhlEZ07bIcLzwR1Win808RIt7zm49VIQ.png"),
            new Subreddit("t5_2qh13", "/r/worldnews/", null),
            new Subreddit("t5_2qlqh", "/r/Android/", "https://b.thumbs.redditmedia.com/fI7UdJ-vgpnLdxy28QdKIYBGg-fEo7KxQ_PS7pn4QzM.png"),

            // currently unavailable
            // new Subreddit("t5_2zmfe", "/r/dankmemes/", "https://b.thumbs.redditmedia.com/qLE6RUF_ARSgCZ854L5Hq4iKd1GqzuW2A5k6xf2kEFs.png"),

            new Subreddit("t5_2tk95", "/r/dataisbeautiful/", "https://a.thumbs.redditmedia.com/PWqqPdsoof5lD4noSANijKfTVDalyChZWQrG9ljigy8.png"),
            new Subreddit("t5_2qh55", "/r/food/", "https://b.thumbs.redditmedia.com/6rCw0815aqJAdAIl3WHdLpkN4EuKlksf87ZY4Hr25Io.png"),
            new Subreddit("t5_2sgp1", "/r/pcmasterrace/", "https://b.thumbs.redditmedia.com/kVZiq5hTApUTyk572gTX7duEalx07VzM9qyrgE6OyAM.png"),
            new Subreddit("t5_2tex6", "/r/ProgrammerHumor/", "https://a.thumbs.redditmedia.com/yrxp4ZA9dSJHqUJDUWe4n-yotZ4chgmzLEm6HFoEej0.png"),
            new Subreddit("t5_2szyo", "/r/Showerthoughts/", "https://b.thumbs.redditmedia.com/_0XpT7iDfFGSAcWbrZOhVHN0HQWymyrsEmfXUf65wVE.png")
    };

    public static List<ISubreddit> getSubreddits() {
        List<ISubreddit> subreddits = new ArrayList<>(SUBREDDITS.length);
        subreddits.addAll(Arrays.asList(SUBREDDITS));
        return subreddits;
    }
}
