package com.we.weblog.mapper;


import com.we.weblog.domain.Post;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *  处理博客信息的管理mapper
 */
@Repository
@Mapper
public interface ContextMapper {


    /**
     * 分类标签查询 这里 in not null去除空列表
     * @return
     */
    @Select({"select DISTINCT categories FROM t_context  where categories is not null"})
    List<String> selectAllCategories();

    @Select({"select * from t_context where type = 'post'  order by uid asc limit 1 "})
    Post getblogId();


    @Select({"select uid,title,created,article from t_context where title='关于我'"})
    Post selectAboutMe();

    /**
     * 得到博客的总数量
      * @return
     */
    @Select({"select count(*) from t_context where type = 'post'"})
    int getBlogNumber();

    /**
     *  插入博客 用于增加博客内容吧
     * @param context
     * @return
     */
    @Insert({"insert into t_context " +
            "(article,title,created,tags,md,type,slug,publish,categories) " +
            "values (#{b.article},#{b.title},#{b.created},#{b.tags},#{b.md}" +
            ",#{b.type},#{b.slug},#{b.publish},#{b.categories})"})
    @SelectKey(before=false,keyProperty="b.uid",resultType=Integer.class,
            statementType= StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id")
    int insertBlog(@Param("b") Post context);

    /**
     *  删除博客
     * @param id
     * @return
     */
    @Delete({"delete from t_context where uid = #{id}"})
    int deleteBlogById(@Param("id") int id);

    /**
     * 批量查询博客  目前10个一次
     * @param count
     * @return
     */
    @Select({"select uid,title,created,tags,article" +
            ",slug,hits from t_context where type = 'post'  limit #{count}"})
    List<Post> getTenBlogs(@Param("count") int count);


    @Update({" update t_context " +
            " set title = #{b.title}," +
            " md = #{b.md}," +
            " slug = #{b.slug}," +
            " categories = #{b.categories},"+
            " tags = #{b.tags},"+
            " article=#{b.article} where uid= #{id}"})
    void updateBlog(@Param("b") Post context, @Param("id") int uid);

    @Update({"update t_context set hits=#{c.hits} where uid = #{c.uid}"})
    void updateHits(@Param("c") Post context);


    @Select({"select uid,title,created,tags from t_context " +
            "where type = 'post'  order by created desc limit #{p},12"})
    List<Post> selectBlogsByYear(@Param("p") int page);


    @Select({"select uid,title,created,tags,categories from t_context " +
            "where type = 'post'  order by tags desc "})
    List<Post> selectBlogsByCategories();


    @Select({"select uid,title,created from t_context " +
            "where type = 'post' order by created desc limit #{p},20"})
    List<Post> getNewBlogs(@Param("p") int page);

    @Select({"select uid,title,article,md,created,tags,hits from t_context where uid = #{id}  "})
    Post getBlogById(@Param("id") int id);

    @Select({"select uid,title,tags,created from t_context " +
            "where uid < #{id} and type = 'post'   order by uid desc limit 1"})
    Post getPreviousBlog(@Param("id") int id);

    @Select({"select uid,title,article,tags,created " +
            "from t_context where uid > #{id} and type = 'post' order by uid asc limit 1"})
    Post getNextBlog(@Param("id") int id);


    @Select("select * from t_context where tags=#{tag}")
    List<Post> selectBlogByTag(@Param("tag") String tagName);


    @Select({"select * from t_context where type= #{type} order by uid desc"})
    List<Post> getPagesByType(@Param("type")String page);

    /**
     * 标签页面删除相关数据
     * @param categoryName
     */
    @Delete({"update t_context set categories = null where categories = #{cate}"})
    int deleleCategoryByName(@Param("cate") String categoryName);
}