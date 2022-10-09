package com.andc.amway.datacubecatcher.controller;

import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.source.SourceManager;
import com.andc.amway.datacubecatcher.persistent.dao.ArticleTotalDetailRepository;
import com.andc.amway.datacubecatcher.persistent.dao.UserCumulateRepository;
import com.andc.amway.datacubecatcher.service.DownloadManager;
import com.andc.amway.datacubecatcher.service.ReportManager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Slf4j
@RestController
@RequestMapping("/api/download")
public class DownloadController {

    @Autowired
    DownloadManager manager;

    @Autowired
    SourceManager sourceManager;

    @Autowired
    ArticleTotalDetailRepository articleTotalDetailRepository;

    @Autowired
    UserCumulateRepository userCumulateRepository;


    //http://localhost:8080/api/download/fansData?accountId=532&start=2017-08-10&end=2017-08-16
    @ApiOperation(value = "下载粉丝数据", notes = "按日期返回期间所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("fansData")
    @SneakyThrows
    public void downloadAtFansData(HttpServletResponse response,
                                   @RequestParam String accountId,
                                   @RequestParam String start,
                                   @RequestParam String end) {

        Optional<Account> accountOptional =
                sourceManager.findAccountByIds(Collections.singletonList(accountId))
                        .stream().findFirst();

        if (!accountOptional.isPresent()) {
            log.error("AccountId has Not exist! -> "+accountId + ", start:"+start+", end:"+end);
            return;
        }

        Account account = accountOptional.get();

        String[] headWord = new String[]{
                "日期", "总粉丝数量",

                "总新增粉丝数", "新增粉丝公众号搜索", "新增粉丝名片分享",
                "新增粉丝扫描二维码", "新增粉丝图文页右上角菜单", "新增粉丝支付后关注",
                "新增粉丝图文页内公众号名称", "新增粉丝公众号文章广告", "新增粉丝朋友圈广告", "新增粉丝其他合计",

                "总取关粉丝数", "取关粉丝公众号搜索", "取关粉丝名片分享",
                "取关粉丝扫描二维码", "取关粉丝图文页右上角菜单", "取关粉丝支付后关注",
                "取关粉丝图文页内公众号名称", "取关粉丝公众号文章广告", "取关粉丝朋友圈广告", "取关粉丝其他合计"};


        List<Object[]> datas = manager.downloadAtFansData(accountId, start, end)
                .stream().map(dto -> {
                    Object[] eachWord = new Object[]{
                            LocalDate.parse(dto.getRef_date()).toString(), String.valueOf(dto.getCumulate_user()),

                            String.valueOf(dto.getNew_user()), String.valueOf(dto.getNew_gzhss()), String.valueOf(dto.getNew_mpfs()),
                            String.valueOf(dto.getNew_smewm()), String.valueOf(dto.getNew_twyysjcd()), String.valueOf(dto.getNew_zfhgz()),
                            String.valueOf(dto.getNew_twyngzhmc()), String.valueOf(dto.getNew_gzhwzgg()), String.valueOf(dto.getNew_pyqgg()),
                            String.valueOf(dto.getNew_qthj()),

                            String.valueOf(dto.getCancel_user()), String.valueOf(dto.getCancel_gzhss()), String.valueOf(dto.getCancel_mpfs()),
                            String.valueOf(dto.getCancel_smewm()), String.valueOf(dto.getCancel_twyysjcd()), String.valueOf(dto.getCancel_zfhgz()),
                            String.valueOf(dto.getCancel_twyngzhmc()), String.valueOf(dto.getCancel_gzhwzgg()), String.valueOf(dto.getCancel_pyqgg()),
                            String.valueOf(dto.getCancel_qthj())

                    };
                    return eachWord;
                }).collect(Collectors.toList());

        this.generate(response, start+"_"+end,"粉丝数据", account, headWord, datas);
    }

    //http://localhost:8080/api/download/fansInteraction?accountId=532&start=2017-08-10&end=2017-08-16
    @ApiOperation(value = "下载粉丝互动数据", notes = "按日期返回期间所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("fansInteraction")
    @SneakyThrows
    public void downloadAtFansInteraction(HttpServletResponse response,
                                          @RequestParam String accountId,
                                          @RequestParam String start,
                                          @RequestParam String end) {

        Optional<Account> accountOptional =
                sourceManager.findAccountByIds(Collections.singletonList(accountId))
                        .stream().findFirst();

        if (!accountOptional.isPresent()) {
            log.error("AccountId has Not exist! -> "+accountId + ", start:"+start+", end:"+end);
            return;
        }

        Account account = accountOptional.get();

        String[] headWord = new String[]{
                "日期",
                "总互动消息数",
                "文字消息数", "图片消息数", "语音消息数",
                "视频消息数", "第三方应用消息数(链接)",

                "总互动消息人数",
                "文字消息人数", "图片消息人数", "语音消息人数",
                "视频消息人数", "第三方应用消息人数(链接)"};


        List<Object[]> datas = manager.downloadAtFansInteraction(accountId, start, end)
                .stream().map(dto -> {
                    Object[] eachWord = new Object[]{
                            LocalDate.parse(dto.getRef_date()).toString(),

                            String.valueOf(dto.getMsg_count_total()),
                            String.valueOf(dto.getMsg_count_wz()), String.valueOf(dto.getMsg_count_tp()), String.valueOf(dto.getMsg_count_yy()),
                            String.valueOf(dto.getMsg_count_sp()), String.valueOf(dto.getMsg_count_dsfyy()),

                            String.valueOf(dto.getMsg_user_total()),
                            String.valueOf(dto.getMsg_user_wz()), String.valueOf(dto.getMsg_user_tp()), String.valueOf(dto.getMsg_user_yy()),
                            String.valueOf(dto.getMsg_user_sp()), String.valueOf(dto.getMsg_user_dsfyy())
                    };
                    return eachWord;
                }).collect(Collectors.toList());

        this.generate(response, start+"_"+end,"粉丝互动数据", account, headWord, datas);
    }

    //http://localhost:8080/api/download/tuwenPushAndShare?accountId=532&start=2017-08-10&end=2017-08-16
    @ApiOperation(value = "下载图文推送,含分享数据", notes = "按日期返回期间所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("tuwenPushAndShare")
    @SneakyThrows
    public void downloadAtTuwenPushAndShare(HttpServletResponse response,
                                    @RequestParam String accountId,
                                    @RequestParam String start,
                                    @RequestParam String end) {

        Optional<Account> accountOptional =
                sourceManager.findAccountByIds(Collections.singletonList(accountId))
                        .stream().findFirst();

        if (!accountOptional.isPresent()) {
            log.error("AccountId has Not exist! -> " + accountId + ", start:" + start + ", end:" + end);
            return;
        }

        Account account = accountOptional.get();

        String[] headWord = new String[]{
                "图文标题",
                "发布日期", "数据日期",
                "送达人数", "图文UV",
                "分享人数", "打开率",
                "分享率"};

        List<Object[]> datas = manager.downloadAtTuwenPush(accountId, start, end)
                .stream().map(dto -> {
                    Object[] eachWord = new Object[]{
                            dto.getTitle(),
                            LocalDate.parse(dto.getRef_date()).toString(), LocalDate.parse(dto.getStat_date()).toString(),
                            String.valueOf(dto.getTarget_user()), String.valueOf(dto.getInt_page_read_user()),
                            String.valueOf(dto.getShare_user()), dto.getInt_page_read_user_rate(),
                            dto.getShare_rate()
                    };
                    return eachWord;
                }).collect(Collectors.toList());

        this.generate(response, start+"_"+end,"图文数据", account, headWord, datas);

    }

    //http://localhost:8080/api/download/tuwenPush?accountId=532&start=2017-08-10&end=2017-08-16
    @ApiOperation(value = "下载图文推送", notes = "按日期返回期间所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("tuwenPush")
    @SneakyThrows
    public void downloadAtTuwenPush(HttpServletResponse response,
                                   @RequestParam String accountId,
                                   @RequestParam String start,
                                   @RequestParam String end) {

        Optional<Account> accountOptional =
                sourceManager.findAccountByIds(Collections.singletonList(accountId))
                        .stream().findFirst();

        if (!accountOptional.isPresent()) {
            log.error("AccountId has Not exist! -> " + accountId + ", start:" + start + ", end:" + end);
            return;
        }

        Account account = accountOptional.get();

        String[] headWord = new String[]{
                "图文标题",
                "发布日期", "数据日期",
                "打开人数", "打开率",
                "送达人数", "送达率"};

        List<Object[]> datas = manager.downloadAtTuwenPush(accountId, start, end)
                .stream().map(dto -> {
                    Object[] eachWord = new Object[]{
                            dto.getTitle(),
                            LocalDate.parse(dto.getRef_date()).toString(), LocalDate.parse(dto.getStat_date()).toString(),
                            String.valueOf(dto.getInt_page_read_user()), dto.getInt_page_read_user_rate(),
                            String.valueOf(dto.getTarget_user()), dto.getTarget_user_rate()
                    };
                    return eachWord;
                }).collect(Collectors.toList());

        this.generate(response, start+"_"+end,"图文数据", account, headWord, datas);

    }

    @ApiOperation(value = "下载图文阅读", notes = "按日期返回期间所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("tuwenRead")
    @SneakyThrows
    public void downloadAtTuwenRead(HttpServletResponse response,
                                    @RequestParam String accountId,
                                    @RequestParam String start,
                                    @RequestParam String end) {

        Optional<Account> accountOptional =
                sourceManager.findAccountByIds(Collections.singletonList(accountId))
                        .stream().findFirst();

        if (!accountOptional.isPresent()) {
            log.error("AccountId has Not exist! -> " + accountId + ", start:" + start + ", end:" + end);
            return;
        }

        Account account = accountOptional.get();

        String[] headWord = new String[]{
                "图文标题",
                "发布日期", "数据日期",
                "总图文页PV",

                "公众号会话PV", "历史消息页PV",
                "朋友圈PV", "好友转发PV",
                "其他场景PV",

                "公众号会话UV", "历史消息页UV",
                "朋友圈UV", "好友转发UV",
                "其他场景UV"};

        List<Object[]> datas = manager.downloadAtTuwenReadAndShareAndAdd(accountId, start, end)
                .stream().map(dto -> {
                    Object[] eachWord = new Object[]{
                            dto.getTitle(),
                            LocalDate.parse(dto.getRef_date()).toString(), LocalDate.parse(dto.getStat_date()).toString(),
                            String.valueOf(dto.getInt_page_read_count()),

                            String.valueOf(dto.getInt_page_from_session_read_count()), String.valueOf(dto.getInt_page_from_hist_msg_read_count()),
                            String.valueOf(dto.getInt_page_from_feed_read_count()), String.valueOf(dto.getInt_page_from_friends_read_count()),
                            String.valueOf(dto.getInt_page_from_other_read_count()),

                            String.valueOf(dto.getInt_page_from_session_read_user()), String.valueOf(dto.getInt_page_from_hist_msg_read_user()),
                            String.valueOf(dto.getInt_page_from_feed_read_user()), String.valueOf(dto.getInt_page_from_friends_read_user()),
                            String.valueOf(dto.getInt_page_from_other_read_user())
                    };
                    return eachWord;
                }).collect(Collectors.toList());

        this.generate(response, start+"_"+end,"图文阅读", account, headWord, datas);

    }

    @ApiOperation(value = "下载图文分享", notes = "按日期返回期间所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("tuwenShare")
    @SneakyThrows
    public void downloadAtTuwenShare(HttpServletResponse response,
                                    @RequestParam String accountId,
                                    @RequestParam String start,
                                    @RequestParam String end) {

        Optional<Account> accountOptional =
                sourceManager.findAccountByIds(Collections.singletonList(accountId))
                        .stream().findFirst();

        if (!accountOptional.isPresent()) {
            log.error("AccountId has Not exist! -> " + accountId + ", start:" + start + ", end:" + end);
            return;
        }

        Account account = accountOptional.get();

        String[] headWord = new String[]{
                "图文标题",
                "发布日期", "数据日期",
                "分享率",

                "总分享次数", "公众号转朋友圈次数",
                "朋友圈转朋友圈次数", "其他场景转发朋友圈次数",

                "总分享人数", "公众号转朋友圈人数",
                "朋友圈转朋友圈人数", "其他场景转发朋友圈人数",};

        List<Object[]> datas = manager.downloadAtTuwenReadAndShareAndAdd(accountId, start, end)
                .stream().map(dto -> {
                    Object[] eachWord = new Object[]{
                            dto.getTitle(),
                            LocalDate.parse(dto.getRef_date()).toString(), LocalDate.parse(dto.getStat_date()).toString(),
                            dto.getShare_rate(),

                            String.valueOf(dto.getShare_count()), String.valueOf(dto.getFeed_share_from_session_cnt()),
                            String.valueOf(dto.getFeed_share_from_feed_cnt()), String.valueOf(dto.getFeed_share_from_other_cnt()),

                            String.valueOf(dto.getShare_user()), String.valueOf(dto.getFeed_share_from_session_user()),
                            String.valueOf(dto.getFeed_share_from_feed_user()), String.valueOf(dto.getFeed_share_from_other_user())
                    };
                    return eachWord;
                }).collect(Collectors.toList());

        this.generate(response, start+"_"+end,"图文分享", account, headWord, datas);

    }

    @ApiOperation(value = "下载图文收藏", notes = "按日期返回期间所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("tuwenAdd")
    @SneakyThrows
    public void downloadAtTuwenAdd(HttpServletResponse response,
                                     @RequestParam String accountId,
                                     @RequestParam String start,
                                     @RequestParam String end) {

        Optional<Account> accountOptional =
                sourceManager.findAccountByIds(Collections.singletonList(accountId))
                        .stream().findFirst();

        if (!accountOptional.isPresent()) {
            log.error("AccountId has Not exist! -> " + accountId + ", start:" + start + ", end:" + end);
            return;
        }

        Account account = accountOptional.get();

        String[] headWord = new String[]{
                "图文标题",
                "发布日期", "数据日期",
                "收藏次数", "收藏人数"};

        List<Object[]> datas = manager.downloadAtTuwenReadAndShareAndAdd(accountId, start, end)
                .stream().map(dto -> {
                    Object[] eachWord = new Object[]{
                            dto.getTitle(),
                            LocalDate.parse(dto.getRef_date()).toString(), LocalDate.parse(dto.getStat_date()).toString(),
                            String.valueOf(dto.getAdd_to_fav_count()), String.valueOf(dto.getAdd_to_fav_user())
                    };
                    return eachWord;
                }).collect(Collectors.toList());

        this.generate(response, start+"_"+end,"图文收藏", account, headWord, datas);

    }


    /**
     * 生成Excel文件
     * @param response
     * @param titleDate
     * @param apiDesc
     * @param account
     * @param headWord
     * @param datas
     * @throws IOException
     */
    private void generate(HttpServletResponse response,
                          String titleDate,
                          String apiDesc,
                          Account account,
                          String[] headWord,
                          List<Object[]> datas) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-disposition", "attachment;filename=Report_"+titleDate+".xls");

        HSSFWorkbook workbook = new HSSFWorkbook();

        // 创建标题样式
        Font headFont = workbook.createFont();
        headFont.setBold(true);
        //headFont.setFontName("微软雅黑");
        headFont.setFontHeightInPoints((short) 10);
        CellStyle headStyle = workbook.createCellStyle();
        headStyle.setFont(headFont);
        headStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

        // 创建每行样式
        Font eachFont = workbook.createFont();
        //eachFont.setFontName("微软雅黑");
        eachFont.setFontHeightInPoints((short) 9);
        CellStyle eachStyle = workbook.createCellStyle();
        eachStyle.setFont(eachFont);

        // 创建百分比样式
        HSSFCellStyle rateCellStyle = workbook.createCellStyle();
        rateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
        rateCellStyle.setFont(eachFont);


        HSSFSheet sheet = workbook.createSheet(account.getName()+"_"+apiDesc);

        // 创建标题行
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < headWord.length; i++) {
            sheet.autoSizeColumn(i);
            Cell cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headWord[i]);
        }

        for (int i = 1; i <= datas.size(); i++) {
            //创建每行
            Object[] eachWord = datas.get(i - 1);

            HSSFRow row = sheet.createRow(i);//创建
            for (int j = 0; j < eachWord.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(eachStyle);
                if (eachWord[j] instanceof String)
                    cell.setCellValue(String.valueOf(eachWord[j]));
                else if (eachWord[j] instanceof Double) {
                    cell.setCellValue(Double.valueOf(eachWord[j].toString()));
                    cell.setCellStyle(rateCellStyle);
                }
            }
        }


        try (OutputStream fOut = response.getOutputStream()) {
            workbook.write(fOut);
        }
    }

}
