package com.itsmite.novels.core.services.user;

import com.itsmite.novels.core.models.user.ReadingSpace;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.repositories.user.ReadingSpaceRepository;
import com.itsmite.novels.core.repositories.user.WritingSpaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserSpaceService {

    private WritingSpaceRepository writingSpaceRepository;
    private ReadingSpaceRepository readingSpaceRepository;

    @Autowired
    public void autowireBeans(WritingSpaceRepository writingSpaceRepository, ReadingSpaceRepository readingSpaceRepository) {
        this.writingSpaceRepository = writingSpaceRepository;
        this.readingSpaceRepository = readingSpaceRepository;
    }

    public ReadingSpace createReadingSpace() {
        return readingSpaceRepository.save(new ReadingSpace());
    }

    public WritingSpace createWritingSpace() {
        return writingSpaceRepository.save(new WritingSpace());
    }
}
