						function createInfoBlock(block_title,
								short_description, block_info) {
							var blockTag = '<div class="infoblock">';
							var titleDiv = '<div class="block_title"><div class="line"><hr></div>'
									+ block_title + '<div class="line"><hr></div></div>';
							var descriptionDiv = '<div class="short_description">'
									+ short_description + '</div>';
							var infoDiv = '<div class="block_info">'
									+ block_info + '</div>';
							var endTag = '</div>';
							return blockTag + titleDiv /*+ descriptionDiv*/
									+ infoDiv + endTag;
						}

						function loadContent(blockNumber, title,description,info) {
							var infoBlock;
							for (var i = 0; i < blockNumber; i++) {
								infoBlock = createInfoBlock(title[i],
										description[i], info[i]);
								$(infoBlock).appendTo('.infogrid');
							}

						}
						function setIconPos() {
							var padLeft = 280;
							var vp_width = $(window).width();
							var left_value_offset = (vp_width - 1024) / 2;
							var datagrid_offset = ($('.infocontentwrapper')
									.width() - $('.info-content').width()) / 2 + 2;

							if (left_value_offset <= padLeft) {
								$(".infogrid").css("left",
										padLeft - datagrid_offset);
							} else {
								$(".infogrid").css("left",
										left_value_offset - datagrid_offset);
							}
						}
						
						function setActiveNavItem(id){
							$(".middleout").removeClass("middelout");
							$(id).addClass("active");
							$(id).children().addClass("middleout");
						}
