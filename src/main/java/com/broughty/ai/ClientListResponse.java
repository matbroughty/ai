package com.broughty.ai;

import java.util.List;

public record ClientListResponse(int pageNumber, int pageSize, int totalResults, List<Client> result) {}