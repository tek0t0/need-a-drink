package bg.softuni.needadrink.service.impl;

import bg.softuni.needadrink.repositiry.CommentRepository;
import bg.softuni.needadrink.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void deleteAllCommentsByUserId(String id) {
        this.commentRepository.deleteAllByAuthor_Id(id);
    }
}
