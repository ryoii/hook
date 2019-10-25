package com.github.ryoii.core.requester;

import com.github.ryoii.core.config.Configuration;

public interface RequesterFactory {

    Requester getRequester(Configuration configuration);
}
