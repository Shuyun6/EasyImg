package com.shuyun.easyimg.core;

import com.shuyun.easyimg.common.Image;
import com.shuyun.easyimg.common.Optional;

public interface ImageCache {

    Optional<Image> obtainImage(String id);

    Optional<Boolean> cacheImage(Image image);

}
