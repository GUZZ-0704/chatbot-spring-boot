package com.example.chatbothexagonal.chatbot.application.port.out;


import com.example.chatbothexagonal.chatbot.infraestructure.external.branch.dto.BranchResponseDTO;

import java.util.List;
import java.util.UUID;

public interface BranchExternalPort {

    BranchResponseDTO getBranchById(UUID branchId);

    List<BranchResponseDTO> listAllBranches();
}
