$(document).ready(function() {
    var svg = d3.select("#svg1"),
        width = +svg.attr("width"),
        height = +svg.attr("height");

    // var names = ['\u534f\u8bae', '\u6570\u636e', '数据', '数据单元', '服务', '硬件'];
    var names = ['标识符', '中文名称', '业务', '设备', '信道', '路由器', '数据', '模式', '术语', '协议', 'default', '参数', '系统', '标识', '模块', '功能', '电路', '网络', '信号', '消息', '接口', '服务', '控制', '终端', '网关'];


    var color = d3.scaleOrdinal(d3.schemeCategory20);
    var colors = ['#FF8C00', '#F4A460', '#FFF5EE', '#FFA07A', '#FF7F50', '#FF4500', '#FF6347', '#FFFF00', '#FFFFE0', '#FFD700', '#FFF8DC', '#F5DEB3', '#00FF7F', '#90EE90', '#32CD32', '#00FF00', '#7FFF00', '#ADFF2F', '#FAFAD2', '#AFEEEE', '#00FFFF', '#7FFFD4', '#00BFFF', '#F0F8FF', '#00CED1'];
    for (var i = 0; i < names.length; i++) {
        $('#indicator').append("<div><span style='background-color:" + colors[i] + "'></span>" + names[i] + "</div>");
    }

    var simulation = d3.forceSimulation()
        .force("link", d3.forceLink().id(function(d) {
            return d.id;
        }))
        .force("charge", d3.forceManyBody())
        .force("center", d3.forceCenter(width / 2, height / 2));

    var graph;
    var root = window.location.href;


    d3.json("http://localhost:8181/l", function(error, data) {
        if (error) throw error;

        graph = data;

        var link = svg.append("g")
            .attr("class", "links")
            .selectAll("line")
            .data(graph.links)
            .enter().append("line")
            .attr("stroke-width", function(d) {
                // return Math.sqrt(d.value);
                return 1;
            });

        var node = svg.append("g")
            .attr("class", "nodes")
            .selectAll("circle")
            .data(graph.nodes)
            .enter().append("circle")
            .attr("r", function(d) {
                return d.size //圆圈大小
            })
            .attr("fill", function(d) {
                // return color(d.group);
                return colors[d.group];  //实体类型
            })
            .attr('stroke', 'none')
            .attr('name', function(d) {
                return d.id;
            })
            .call(d3.drag()
                .on("start", dragstarted)
                .on("drag", dragged)
                .on("end", dragended));

        var text = svg.append("g")
            .attr("class", "texts")
            .selectAll("text")
            .data(graph.nodes)
            .enter().append("text")
            .attr("font-size", function(d) {
                return d.size
            })
            .attr("fill", function(d) {
                // return color(d.group);
                return colors[d.group];
            })
            .attr('name', function(d) {
                return d.id;
            })
            .text(function(d) {
                return d.id;
            })
            .attr('text-anchor', 'middle')
            .call(d3.drag()
                .on("start", dragstarted)
                .on("drag", dragged)
                .on("end", dragended));

        node.append("title")
            .text(function(d) {
                return d.id;
            });

        simulation
            .nodes(graph.nodes)
            .on("tick", ticked);

        simulation.force("link")
            .links(graph.links);

        function ticked() {
            link
                .attr("x1", function(d) {
                    return d.source.x;
                })
                .attr("y1", function(d) {
                    return d.source.y;
                })
                .attr("x2", function(d) {
                    return d.target.x;
                })
                .attr("y2", function(d) {
                    return d.target.y;
                });

            node
                .attr("cx", function(d) {
                    return d.x;
                })
                .attr("cy", function(d) {
                    return d.y;
                });

            text.
            attr('transform', function(d) {
                return 'translate(' + d.x + ',' + (d.y + d.size / 2) + ')';
            });
        }
    });
    var dragging = false;

    function dragstarted(d) {
        if (!d3.event.active) simulation.alphaTarget(0.3).restart();
        d.fx = d.x;
        d.fy = d.y;
        dragging = true;
    }

    function dragged(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
    }

    function dragended(d) {
        if (!d3.event.active) simulation.alphaTarget(0);
        d.fx = null;
        d.fy = null;
        dragging = false;
    }

    $('#mode span').click(function(event) {
        $('#mode span').removeClass('active');
        $(this).addClass('active');
        if ($(this).text() == 'Circles') {
            $('.texts text').hide();
            $('.nodes circle').show();
        } else {
            $('.texts text').show();
            $('.nodes circle').hide();
        }
    });

    $('#svg1').on('mouseenter', '.nodes circle', function(event) {
        if (!dragging) {
            var name = $(this).attr('name');

            $('#info h4').css('color', $(this).attr('fill')).text(name);
            $('#info p').remove();
            for (var key in info[name]) {
                if (typeof(info[name][key]) == 'object') {
                    continue;
                }
                if (key == 'id' || key == 'class' || key == 'name' ) {
                    continue;
                }
                $('#info').append('<p><span>' + key + '</span>' + info[name][key] + '</p>');
            }

            d3.select('#svg1 .nodes').selectAll('circle').attr('class', function(d) {
                if (d.id == name) {
                    return '';
                }

                for (var i = 0; i < graph.links.length; i++) {
                    if (graph.links[i]['source'].id == name && graph.links[i]['target'].id == d.id) {
                        return '';
                    }
                    if (graph.links[i]['target'].id == name && graph.links[i]['source'].id == d.id) {
                        return '';
                    }
                }
                return 'inactive';
            });
            d3.select("#svg1 .links").selectAll('line').attr('class', function(d) {
                if (d.source.id == name || d.target.id == name) {
                    return '';
                } else {
                    return 'inactive';
                }
            });
        }
    });

    $('#svg1').on('mouseleave', '.nodes circle', function(event) {
        if (!dragging) {
            d3.select('#svg1 .nodes').selectAll('circle').attr('class', '');
            d3.select('#svg1 .links').selectAll('line').attr('class', '');
        }
    });

    $('#svg1').on('mouseenter', '.texts text', function(event) {
        if (!dragging) {
            var name = $(this).attr('name');

            $('#info h4').css('color', $(this).attr('fill')).text(name);

            $('#info p').remove();
            for (var key in info[name]) {
                console.log(key)

                if (typeof(info[name][key]) == 'object') {
                    continue;
                }
                if (key == 'id' || key == 'class' || key == 'name') {
                    continue;
                }
                $('#info').append('<p><span>' + key + '</span>' + info[name][key] + '</p>');
            }
            //$('#info').append('<a href="/links"><p><span>' + '操作'+ '</span>' + '</p></a>s');

            d3.select('#svg1 .texts').selectAll('text').attr('class', function(d) {
                if (d.id == name) {
                    return '';
                }

                for (var i = 0; i < graph.links.length; i++) {
                    if (graph.links[i]['source'].id == name && graph.links[i]['target'].id == d.id) {
                        return '';
                    }
                    if (graph.links[i]['target'].id == name && graph.links[i]['source'].id == d.id) {
                        return '';
                    }
                }
                return 'inactive';
            });
            d3.select("#svg1 .links").selectAll('line').attr('class', function(d) {
                if (d.source.id == name || d.target.id == name) {
                    return '';
                } else {
                    return 'inactive';
                }
            });
        }
    });

    $('#svg1').on('mouseleave', '.texts text', function(event) {
        if (!dragging) {
            d3.select('#svg1 .texts').selectAll('text').attr('class', '');
            d3.select('#svg1 .links').selectAll('line').attr('class', '');
        }
    });

    $('#search1 input').keyup(function(event) {
        if ($(this).val() == '') {
            d3.select('#svg1 .texts').selectAll('text').attr('class', '');
            d3.select('#svg1 .nodes').selectAll('circle').attr('class', '');
            d3.select('#svg1 .links').selectAll('line').attr('class', '');
        } else {
            var name = $(this).val();
            d3.select('#svg1 .nodes').selectAll('circle').attr('class', function(d) {
                if (d.id.toLowerCase().indexOf(name.toLowerCase()) >= 0) {
                    return '';
                } else {
                    return 'inactive';
                }
            });
            d3.select('#svg1 .texts').selectAll('text').attr('class', function(d) {
                if (d.id.toLowerCase().indexOf(name.toLowerCase()) >= 0) {
                    return '';
                } else {
                    return 'inactive';
                }
            });
            d3.select("#svg1 .links").selectAll('line').attr('class', function(d) {
                return 'inactive';
            });
        }
    });



    $('#svg2').on('mouseenter', 'g.row', function(event) {
        event.preventDefault();
        $('#svg2 text.title').attr('fill-opacity',0);
        console.log($(this).attr('id'));
        $('#svg2 text.title[name="' + $(this).attr('id') + '"]').attr('fill-opacity',1);
    });

    $('#svg2').on('mouseleave', 'g.row', function(event) {
        event.preventDefault();
        $('#svg2 text.title').attr('fill-opacity',0);
    });

    var info;

    d3.json("http://localhost:8181/e", function(error, data) {
        info = data;
        console.log(info)
    });
});