package com.worktree.hrms.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface LogsService {

    byte[] downloadLogs(Optional<Integer> lines) throws IOException;

    List<String> getLatestLogs(Optional<Integer> lines) throws IOException;

}
