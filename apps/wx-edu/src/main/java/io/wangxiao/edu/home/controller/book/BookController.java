package io.wangxiao.edu.home.controller.book;

import io.wangxiao.commons.entity.PageEntity;
import io.wangxiao.commons.util.ObjectUtils;
import io.wangxiao.edu.common.util.DateEditor;
import io.wangxiao.edu.home.common.EduBaseController;
import io.wangxiao.edu.home.entity.Book.Book;
import io.wangxiao.edu.home.entity.Book.BookDTO;
import io.wangxiao.edu.home.entity.Book.QueryBookCondition;
import io.wangxiao.edu.home.service.book.BookService;
import io.wangxiao.edu.sysuser.entity.QuerySubject;
import io.wangxiao.edu.sysuser.entity.Subject;
import io.wangxiao.edu.sysuser.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class BookController extends EduBaseController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private static final String getBookList = getViewPath("/book/book-list");// 图书添加页面
    private static final String getBookInfo = getViewPath("/book/book-infor");// 图书添加页面


    @Autowired
    private SubjectService subjectService;
    @Autowired
    private BookService bookService;

    // 绑定变量名参数
    @InitBinder("queryBookCondition")
    public void initBinderQueryBookCondition(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.setFieldDefaultPrefix("queryBookCondition.");
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    /**
     * 查询图书列表
     *
     * @param request
     * @param model
     * @param queryBookCondition
     * @param page
     * @return
     */
    @RequestMapping("/book/list")
    public String getBookList(HttpServletRequest request, Model model, @ModelAttribute QueryBookCondition queryBookCondition, @ModelAttribute("page") PageEntity page) {
        try {
            // 查询一级专业
            List<Subject> subjectList = subjectService.getSubjectOneList();
            // 查询条件专业
            Subject subject = new Subject();
            if (ObjectUtils.isNull(queryBookCondition) || ObjectUtils.isNull(queryBookCondition.getBookSubjectid())) {
                queryBookCondition.setBookSubjectid(0);
            }
            subject.setSubjectId(Long.valueOf(queryBookCondition.getBookSubjectid()));
            subject = subjectService.getSubjectBySubjectId(subject);
            String subjects = "";
            if (ObjectUtils.isNotNull(subject) && subject.getParentId() == 0) {
                QuerySubject querySubject = new QuerySubject();
                List<Subject> allSubjects = subjectService.getSubjectList(querySubject);
                subjects = getRecursiveSubject(allSubjects, subject.getSubjectId());
                subjects += subject.getSubjectId();
                queryBookCondition.setSujectIds(subjects);
                queryBookCondition.setBookSubjectid(0);
            }
            // 设置分页

            // 查询图书列表
            List<BookDTO> bookList = bookService.getbookDTOList(queryBookCondition, page);
            model.addAttribute("subjectList", subjectList);
            model.addAttribute("subject", subject);
            model.addAttribute("bookList", bookList);
            model.addAttribute("page", page);
        } catch (Exception e) {
            logger.error("getBookList", e);
            return setExceptionRequest(request, e);
        }
        return getBookList;
    }


    /**
     * 查找子专业方法
     */
    @RequestMapping("/book/subject/{id}")
    @ResponseBody
    public Map<String, Object> getTwoSubjects(HttpServletRequest request, @PathVariable Long id) {
        Map<String, Object> json = null;
        try {
            List<Subject> subjects = subjectService.getSubjectListByOne(id);
            json = this.getJsonMap(true, "true", subjects);
        } catch (Exception e) {
            logger.error("BookController.getTwoSubjects", e);
            json = this.getJsonMap(false, "error", null);
        }
        return json;
    }

    /**
     * 查询图书详情
     *
     * @param request
     * @param model
     * @param bookId
     * @return
     */
    @RequestMapping("/book/info/{bookId}")
    public String getBookInfo(HttpServletRequest request, Model model, @PathVariable("bookId") Integer bookId, RedirectAttributes redirectAttributes) {
        try {
            QueryBookCondition queryBookCondition = new QueryBookCondition();
            queryBookCondition.setBookId(bookId);
            // 查询图书详情
            Book book = bookService.queryBookById(queryBookCondition);
            if (ObjectUtils.isNull(book)) {
                redirectAttributes.addAttribute("msg", "对不起该图书已经下架或者删除");
                return "redirect:/front/success";
            }
            // 查询同类图书
            queryBookCondition.setBookSubjectid(book.getBookSubjectid());
            queryBookCondition.setOrderNum("5");// 按出版时间排序
            PageEntity page = new PageEntity();
            page.setPageSize(5);
            List<BookDTO> bookList = bookService.getbookDTOList(queryBookCondition, page);
            model.addAttribute("book", book);
            model.addAttribute("bookList", bookList);
        } catch (Exception e) {
            logger.error("getBookInfo", e);
            return setExceptionRequest(request, e);
        }
        return getBookInfo;
    }

    /**
     * 查詢所有子专业
     *
     * @param subjectList
     * @param subjectId
     * @return
     */
    public String getRecursiveSubject(List<Subject> subjectList, Long subjectId) {
        String subjects = "";
        if (ObjectUtils.isNotNull(subjectList) && subjectList.size() > 0) {
            for (Subject subject : subjectList) {
                if (subject.getParentId().longValue() == subjectId.longValue()) {
                    subjects += subject.getSubjectId() + ",";
                    getRecursiveSubject(subjectList, subject.getSubjectId());
                }
            }
        }
        return subjects;
    }
}