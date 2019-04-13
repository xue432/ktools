package com.kalvin.ktools.dao;

        import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
        import com.kalvin.ktools.dto.CommentDTO;
        import com.kalvin.ktools.entity.Comment;
        import com.baomidou.mybatisplus.core.mapper.BaseMapper;
        import org.apache.ibatis.annotations.Param;

        import java.util.List;

/**
 * <p>
 * 工具评论表 Mapper 接口
 * </p>
 *
 * @author Kalvin
 * @since 2019-04-12
 */
public interface CommentDao extends BaseMapper<Comment> {

    /**
     * 查询用户评论列表，带用户信息
     * @param menuId 菜单ID
     * @param page 分页
     * @return
     */
    List<CommentDTO> selectCommentsWithUser(Long menuId, Page page);

}
