package com.worktree.hrms.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface LogsService {

    byte[] downloadLogs(Optional<Integer> lines) throws IOException;

}
