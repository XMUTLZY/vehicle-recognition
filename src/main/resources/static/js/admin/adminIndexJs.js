$(function () {
    adminIndexJs.bindEvent();
});
var adminIndexJs = {
    bindEvent: function () {
        adminIndexJs.event.uploadImageTag();
        adminIndexJs.event.uploadImageClassify();
        adminIndexJs.event.uploadImageTest();
        adminIndexJs.event.getData();
    },
    event: {
        getData: function () {
            $("#get-data").removeClass('layui-hide');
            $("#data-source-sign").addClass('layui-hide');
            $("#classify").addClass('layui-hide');
            $("#math-test").addClass('layui-hide');
        },
        dataSourceSign: function () {
            layui.use(['table', 'form'], function () {
                var table = layui.table;
                var form = layui.form;
                $("#data-source-sign").removeClass('layui-hide');
                $("#get-data").addClass('layui-hide');
                $("#classify").addClass('layui-hide');
                $("#math-test").addClass('layui-hide');
                table.render({
                    elem: '#data-source-sign-table'
                    , height: 500
                    , width: 920
                    , url: '/image/allImage'
                    , page: true
                    , limits: [5, 10, 20]
                    , limit: 10
                    , cols: [[
                        {field: 'id', title: 'Id', sort: true, width: 120, fixed: 'left'}
                        , {
                            field: 'image', title: '数据集图片', align: 'center', width: 200, templet: function (d) {
                                return '<div onclick="adminIndexJs.method.show_img(this)" ><img src="' + d.image + '" alt="" width="100px" height="100px"></a></div>';
                            }
                        }
                        , {field: 'createTime', title: '创建时间', width: 200}
                        , {field: 'updateTime', title: '修改时间', width: 200}
                        , {
                            field: 'isSign',
                            title: '是/否有车辆',
                            align: 'center',
                            width: 200,
                            templet: "#data-source-sign-tool",
                            unresize: true
                        }
                    ]]
                });
                form.on('switch(switch-car)', function (obj) {
                    layer.tips(this.value + ' ' + this.name + '：' + obj.elem.checked, obj.othis);
                    data = {};
                    data.id = this.value;
                    if (obj.elem.checked == true) {
                        data.isSign = 1;
                    } else {
                        data.isSign = 0;
                    }
                    $.ajax({
                        url: '/image/updateImage',
                        type: 'post',
                        contentType: 'application/json',
                        data: JSON.stringify(data),
                        success: function (result) {

                        },
                        error: function () {
                            layer.msg("数据请求异常");
                        }
                    })
                })
            })
        },
        classify: function () {
            $("#classify").removeClass('layui-hide');
            $("#get-data").addClass('layui-hide');
            $("#data-source-sign").addClass('layui-hide');
            $("#math-test").addClass('layui-hide');
        },
        mathTest: function () {
            $("#math-test").removeClass('layui-hide');
            $("#get-data").addClass('layui-hide');
            $("#data-source-sign").addClass('layui-hide');
            $("#classify").addClass('layui-hide');
        },
        uploadImageTag: function () {
            layui.use('upload', function () {
                var $ = layui.jquery
                    , upload = layui.upload;
                //普通图片上传
                upload.render({
                    elem: '#image-upload-tag'
                    , url: '/image/uploadImage'
                    , accept: 'images'
                    , multiple: true
                    , data: {
                        type: 0
                    }
                    , before: function (obj) {
                        layer.load();
                    }
                    , allDone: function (res) {
                        layer.closeAll('loading');
                        adminIndexJs.event.dataSourceSign();
                    }
                    , error: function (index, upload) {
                        layer.msg("错误");
                    }
                });
            });
        },
        uploadImageClassify: function () {
            layui.use('upload', function () {
                var $ = layui.jquery
                    , upload = layui.upload;
                //普通图片上传
                upload.render({
                    elem: '#image-upload-classify'
                    , url: '/image/uploadImage'
                    , accept: 'images'
                    , data: {
                        type: 1
                    }
                    , before: function (obj) {
                        layer.load();
                    }
                    , done: function (res) {
                        layer.closeAll('loading');
                        adminIndexJs.event.classify();
                        $("#image-upload-classify-img").attr("src", res.msg);
                    }
                    , error: function (index, upload) {
                        layer.msg("错误");
                    }
                });
            });
        },
        uploadImageTest: function () {
            layui.use('upload', function () {
                var $ = layui.jquery
                    , upload = layui.upload;
                //普通图片上传
                upload.render({
                    elem: '#image-upload-test'
                    , url: '/image/uploadImage'
                    , accept: 'images'
                    , data: {
                        type: 1
                    }
                    , before: function (obj) {
                        layer.load();
                    }
                    , done: function (res) {
                        layer.closeAll('loading');
                        $("#image-upload-test-img").attr("src", res.msg);
                    }
                    , error: function (index, upload) {
                        layer.msg("错误");
                    }
                });
            });
        }
    },
    method: {
        show_img: function (t) {
            var t = $(t).find("img");
            //页面层
            layer.open({
                type: 1,
                title: '头像',
                skin: 'layui-layer-rim', //加上边框
                area: ['80%', '80%'], //宽高
                shadeClose: true, //开启遮罩关闭
                end: function (index, layero) {
                    return false;
                },
                content: '<div style="text-align:center"><img src="' + $(t).attr('src') + '" /></div>'
            });
        },
        startRun: function () {
            layer.load(2, {
                content: '爬取数据中',
                success: function (layero) {
                    layero.find('.layui-layer-content').css({
                        'paddingTop': '40px',
                        'width': '60px',
                        'textAlign': 'center',
                        'backgroundPositionX': 'center'
                    });
                }
            });
            $.ajax({
                url: '/image/saveToMysql',
                contentType: 'application/json',
                type: 'post',
                success: function (result) {
                    $("#data-time").val(result.msg);
                    $("#data-time").attr("disabled", "disabled");
                    adminIndexJs.method.saveImage();
                    layer.closeAll('loading');
                },
                error: function (result) {
                }
            })
        },
        recover: function () {
            $("#data-time").removeAttr("disabled", "disabled");
            $("#data-time").val(null);
        },
        saveImage: function () {
            $.ajax({
                url: 'image/saveImage',
                contentType: 'application/json',
                type: 'post',
                success: function (result) {
                    layer.msg('爬取数据成功');
                },
                error: function (result) {
                    layer.msg("爬取数据成功");
                }
            })
        },
        startClassify: function () {
            if ($("#image-upload-classify-img")[0].src == "") {
                layer.msg("请先上传需要分类的图片");
                return;
            }
            layer.load();
            $.ajax({
                url: '/image/classifyImage',
                contentType: 'application/json',
                data: JSON.stringify({
                    image:$("#image-upload-classify-img")[0].src
                }),
                type: 'post',
                success: function (result) {
                    layer.closeAll('loading');
                    $("#image-upload-classify-img").attr("src", result.msg);
                },
                error: function () {
                    layer.closeAll('loading');
                    layer.msg("数据请求异常");
                }
            })
        },
        startTest: function () {
            if ($("#image-upload-test-img")[0].src == "") {
                layer.msg("请先上传需要测试的图片");
                return;
            }
            layer.load();
            $.ajax({
                url: '/image/testImage',
                contentType: 'application/json',
                data: JSON.stringify({
                    image: $("#image-upload-test-img")[0].src
                }),
                type: 'post',
                success: function (result) {
                    layer.closeAll('loading');
                    $("#image-test-current").val(result.resultMap["project1"]);
                    $("#image-test-1").val(result.resultMap["project2"]);
                    $("#image-test-2").val(result.resultMap["project3"]);
                },
                error: function () {
                    layer.closeAll('loading');
                    layer.msg("数据请求异常");
                }
            })
        }
    }
}
