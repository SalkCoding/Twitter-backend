package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.BlockOutput;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.BlockService;
import com.salkcoding.twitter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlockController {

    private final UserService userService;
    private final BlockService blockService;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/block")
    public String blockPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            @ModelAttribute("errorMessage") String message,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        if (message != null && !message.isEmpty() && !message.isBlank())
            model.addAttribute("failMessage", message);

        List<BlockOutput> blockList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Calendar loginTime = Calendar.getInstance();
        blockService.getBlockList(user.getUserId()).forEach(block -> {
            calendar.setTimeInMillis(block.getBlockedTime());
            loginTime.setTimeInMillis(userService.getUserLastLogin(block.getTargetId()));
            blockList.add(
                    new BlockOutput(
                            block.getBlockId(),
                            block.getTargetId(),
                            block.getBlockerId(),
                            simpleDateFormat.format(calendar.getTime()),
                            simpleDateFormat.format(loginTime.getTime())
                    )
            );
        });
        model.addAttribute("block", blockList);

        return "block";
    }

    @PostMapping("/block/create")
    public String blockCreatePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes,
            String targetId
    ) {
        if (user == null) return "redirect:/login";

        if (blockService.isBlocked(targetId, user.getUserId())) {
            redirectAttributes.addFlashAttribute("failMessage", "Already block that user!");
            return "redirect:" + httpServletRequest.getHeader("Referer");
        }

        if (!blockService.addBlock(targetId, user.getUserId()))
            redirectAttributes.addFlashAttribute("failMessage", "Invalid user ID!");

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @PostMapping("/block/delete")
    public String blockDeletePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest httpServletRequest,
            long blockId
    ) {
        if (user == null) return "redirect:/login";

        blockService.deleteBlock(blockId);

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

}
