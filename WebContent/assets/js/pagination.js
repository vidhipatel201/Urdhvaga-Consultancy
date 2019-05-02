/*var jBasePath = "";
var jOrder = 0;
var jPageNo = 1;
var jLimit = 15;
var jPageCount = 0;
var jSource = "";

var jSorting = "";
var jSortingId = "";
var divId = "";
var jTableId = "";
var jPaginationId = "";
var jNextButtonId = "";
var jPreviousButtonId = "";
var jPageNoId = "";
var jFilterOptions = "";
var jFilters = [];
var jFilterId = "";
var jExtraParameters = "";*/

var jBegin = true;
var jBegin2 = false;

(function($){
	jQuery.fn.extend({

		jPagination: function(options) {
			
			var defaults = {				
				basePath: "",
				order: 0,
				pageNo: 1,
				limit: 15,
				limiting: "",
				pageCount: 0,
				source: "",
				sorting: "",
				filter: "",
				filtersApplied: [],
				extraParameters: ""
			};  
			var options = $.extend(defaults, options);  
			
			var pluginElement = this[0];
			var plugin = pluginElement.id;
			
			$("#" + plugin).data("basePath", options.basePath);
			$("#" + plugin).data("order", options.order);
			$("#" + plugin).data("pageNo", options.pageNo);
			$("#" + plugin).data("limit", options.limit);
			$("#" + plugin).data("limiting", options.limiting);
			$("#" + plugin).data("pageCount", options.pageCount);
			$("#" + plugin).data("source", options.source);
			$("#" + plugin).data("sorting", options.sorting);
			$("#" + plugin).data("filter", options.filter);
			$("#" + plugin).data("filtersApplied", options.filtersApplied);
			$("#" + plugin).data("extraParameters", options.extraParameters);
			
			return this.each(function() {
				
					var jSortingId = plugin + "-sorting-options";
					var jTableId = plugin + "-table";
					var jPaginationId = plugin + "-pagination";
					var jNextButtonId = plugin + "-nextButton";
					var jPreviousButtonId = plugin + "-previousButton";
					var jPageNoId = plugin + "-pages";
					var jFilterId = plugin + "-filter";
					var jLimitId = plugin + "-limit";
					
				if($("#" + jSortingId).length == 0) {
					jBegin = true;
					$("#" + plugin).html("<div class=\"sorting-options\" id=\"" + jLimitId + "\"></div><div class=\"sorting-options\" id=\"" + jSortingId + "\"></div><div class=\"sorting-options\" id=\"" + jFilterId + "\"></div><div style=\"width:100%;display:inline-block\" id=\"" + jTableId + "\"></div><div id=\"" + jPaginationId + "\"></div>");
				}
				$("#" + jTableId).html('<img id="loader" style="display:block;margin:auto" src="'+$("#" + plugin).data("basePath")+'/assets/img/loader.gif">');
				var jData = {};
				if($("#" + plugin).data("order") == 0) {
					if($("#" + plugin).data("filtersApplied") == [] || $("#" + plugin).data("filtersApplied").length === 0) {
						jData = {
					    		'pageNo': $("#" + plugin).data("pageNo"),
					    		'limit': $("#" + plugin).data("limit"),
					    		'begin': (jBegin2 ? true : jBegin),
					    		'isFiltered': false,
					    		'extraParameters': $("#" + plugin).data("extraParameters")
						};
					} else {
						jData = {
					    		'pageNo': $("#" + plugin).data("pageNo"),
					    		'limit': $("#" + plugin).data("limit"),
					    		'begin': (jBegin2 ? true : jBegin),
					    		'filters': $("#" + plugin).data("filtersApplied"),
					    		'isFiltered': true,
					    		'extraParameters': $("#" + plugin).data("extraParameters")
						};
					}
				} else {
					if($("#" + plugin).data("filtersApplied") == [] || $("#" + plugin).data("filtersApplied").length === 0) {
						jData = {
								'order': $("#" + plugin).data("order"),
								'pageNo': $("#" + plugin).data("pageNo"),
					    		'limit': $("#" + plugin).data("limit"),
					    		'begin': (jBegin2 ? true : jBegin),
					    		'isFiltered': false,
					    		'extraParameters': $("#" + plugin).data("extraParameters")
						};
					} else {
						jData = {
								'order': $("#" + plugin).data("order"),
								'pageNo': $("#" + plugin).data("pageNo"),
					    		'limit': $("#" + plugin).data("limit"),
					    		'begin': (jBegin2 ? true : jBegin),
					    		'filters': $("#" + plugin).data("filtersApplied"),
					    		'isFiltered': true,
					    		'extraParameters': $("#" + plugin).data("extraParameters")
						};
					}
				}
				
				$.ajax({
			    	url: $("#" + plugin).data("source"),
			    	data: jData,
			    	dataType: 'json',	
			    	method: "POST",
			    	beforeSend: function() {
			    		$("#" + jTableId).html('<img id="loader" style="display:block;margin:auto" src="'+$("#" + plugin).data("basePath")+'/assets/img/loader.gif">');
			    	},
			    	success: function(data) {
			    			jBegin2 = false;
				    		$("#" + jTableId).html(data.table);
				    		$("#" + plugin).data("pageCount", data.pageCount);
				    		if(jBegin && data.pageCount != 0) {
				    			jBegin = false;
//				    			$("#" + plugin).data("pageCount", data.pageCount);
				    			//if(jOrder != 0) {
				    			if($("#" + plugin).data("sorting") != "" || $("#" + plugin).data("sorting").length !== 0) {
				    				var sortingList = '<select class=\"btn btn-default sort-dropdown\" id="'+jSortingId+'-select">';
					    			sortingList += '<option selected disabled hidded>Sort results by</options>';
					    			for(var i = 0; i < $("#" + plugin).data("sorting").length; i++) {
					    			    sortingList += '<option value="' + $("#" + plugin).data("sorting")[i][0] + '">' + $("#" + plugin).data("sorting")[i][1]  + '</option>'; 
					    			}
					    			sortingList += '</select>';
					    			$("#" + jSortingId).html(sortingList);
					    			
					    			$(document).on('change', "#" + jSortingId+"-select", function() {
					    				$("#" + plugin).data("order", this.value);
					    				jBegin2 = true;
					    				$("#" + plugin).jPagination({
					    					basePath: $("#" + plugin).data("basePath"),
					    					order: $("#" + plugin).data("order"),
					    					pageNo: $("#" + plugin).data("pageNo"),
					    					limit: $("#" + plugin).data("limit"),
					    					pageCount: $("#" + plugin).data("pageCount"),
					    					source: $("#" + plugin).data("source"),
					    					sorting: $("#" + plugin).data("sorting"),
					    					filter: $("#" + plugin).data("filter"),
					    					filtersApplied: $("#" + plugin).data("filtersApplied"),
					    					extraParameters: $("#" + plugin).data("extraParameters")
					    				});
					    			});
				    			}
				    			
				    			if($("#" + plugin).data("limiting") != "" || $("#" + plugin).data("limiting").length !== 0) {
				    				var limitList = '<select class=\"btn btn-default sort-dropdown\" id="'+jLimitId+'-select">';
					    			limitList += '<option selected disabled hidded>Limit Results</options>';
					    			for(var i = 0; i < $("#" + plugin).data("limiting").length; i++) {
					    			    limitList += '<option value="' + $("#" + plugin).data("limiting")[i][0] + '">' + $("#" + plugin).data("limiting")[i][1]  + '</option>'; 
					    			}
					    			limitList += '</select>';
					    			$("#" + jLimitId).html(limitList);
					    			
					    			$(document).on('change', "#" + jLimitId+"-select", function() {
					    				$("#" + plugin).data("limit", this.value);
					    				jBegin2 = true;
					    				$("#" + plugin).jPagination({
					    					basePath: $("#" + plugin).data("basePath"),
					    					order: $("#" + plugin).data("order"),
					    					pageNo: $("#" + plugin).data("pageNo"),
					    					limit: $("#" + plugin).data("limit"),
					    					pageCount: $("#" + plugin).data("pageCount"),
					    					source: $("#" + plugin).data("source"),
					    					sorting: $("#" + plugin).data("sorting"),
					    					filter: $("#" + plugin).data("filter"),
					    					filtersApplied: $("#" + plugin).data("filtersApplied"),
					    					extraParameters: $("#" + plugin).data("extraParameters")
					    				});
					    			});
				    			}
				    			
				    			if($("#" + plugin).data("filter") != "" || $("#" + plugin).data("filter").length !== 0) {
				    				var filterList = '<select id="'+jFilterId+'-select" multiple="multiple">';
					    			filterList += '<option selected disabled hidded>Filter results by</options>';
					    			for(var i = 0; i < $("#" + plugin).data("filter").length; i++) {
					    			    filterList += '<option value="' + $("#" + plugin).data("filter")[i][0] + '">' + $("#" + plugin).data("filter")[i][1]  + '</option>'; 
					    			}
					    			filterList += '</select>';
					    			$("#" + jFilterId).html(filterList);
					    			$("#" + jFilterId + "-select").multiselect({
					    				   
					    				   onChange: function(element, checked) {
					    					   $("#" + plugin).data("pageNo", 1);
					    					   jBegin2 = true;
					    		               if (checked === true) {
					    		            	   $("#" + plugin).data("filtersApplied").push(element.val());
					    		               } else if (checked === false) {
					    		            	   var index = $("#" + plugin).data("filtersApplied").indexOf(element.val());
					    		            	   if (index > -1) {
					    		            		   $("#" + plugin).data("filtersApplied").splice(index, 1);
					    		            	   }
					    		               } 
					    		               
					    		               $("#" + plugin).jPagination({
							    					basePath: $("#" + plugin).data("basePath"),
							    					order: $("#" + plugin).data("order"),
							    					pageNo: $("#" + plugin).data("pageNo"),
							    					limit: $("#" + plugin).data("limit"),
							    					pageCount: $("#" + plugin).data("pageCount"),
							    					source: $("#" + plugin).data("source"),
							    					sorting: $("#" + plugin).data("sorting"),
							    					filter: $("#" + plugin).data("filter"),
							    					filtersApplied: $("#" + plugin).data("filtersApplied"),
							    					extraParameters: $("#" + plugin).data("extraParameters")
							    				});
					    		           } 
					    				   
					    			   }); 
				    			}
				    			
				    			var navButtons = "<nav aria-label=\"pagination\"><ul class=\"pagination justify-content-center\">";
				    			navButtons += "<li style=\"cursor:pointer\" class=\"page-item disabled previousButton\" id=\""+ jPreviousButtonId +"\"><a class=\"page-link\">Previous</a></li>";
				    			if($("#" + plugin).data("pageCount") == 1) {
				    				navButtons += "<li class=\"page-item disabled nextButton\" id=\""+ jNextButtonId +"\" style=\"cursor:pointer\"><a class=\"page-link\">Next</a></li></ul></nav>";
				    			} else {
				    				navButtons += "<li class=\"page-item nextButton\" id=\""+ jNextButtonId +"\"  style=\"cursor:pointer\"><a class=\"page-link\">Next</a></li></ul>";
				    			}
				    			navButtons += "<div id=\""+jPageNoId+"\">Showing page " + $("#" + plugin).data("pageNo") + " of " + $("#" + plugin).data("pageCount") + " pages.</div>";
				    			$("#" + jPaginationId).html(navButtons);
				    			
				    			
				    			
				    			$(document).on('click', "#" + jNextButtonId, function() {
				    				if($("#" + plugin).data("pageNo") == $("#" + plugin).data("pageCount")) {
				    					if(!$("#" + jNextButtonId).hasClass("disabled")) {
				    						$("#" + jNextButtonId).addClass("disabled");
				    					}
				    				} else {
				    					if($("#" + jNextButtonId).hasClass("disabled")) {
				    						$("#" + jNextButtonId).removeClass("disabled");
				    					}
				    					$("#" + plugin).data("pageNo", $("#" + plugin).data("pageNo")+1);
				    					if($("#" + plugin).data("pageNo") == $("#" + plugin).data("pageCount")) {
					    					if(!$("#" + jNextButtonId).hasClass("disabled")) {
					    						$("#" + jNextButtonId).addClass("disabled");
					    					}
					    				}
				    					if($("#" + jPreviousButtonId).hasClass("disabled")) {
				    						$("#" + jPreviousButtonId).removeClass("disabled");
				    					}
				    					 $("#" + plugin).jPagination({
						    					basePath: $("#" + plugin).data("basePath"),
						    					order: $("#" + plugin).data("order"),
						    					pageNo: $("#" + plugin).data("pageNo"),
						    					limit: $("#" + plugin).data("limit"),
						    					pageCount: $("#" + plugin).data("pageCount"),
						    					source: $("#" + plugin).data("source"),
						    					sorting: $("#" + plugin).data("sorting"),
						    					filter: $("#" + plugin).data("filter"),
						    					filtersApplied: $("#" + plugin).data("filtersApplied"),
						    					extraParameters: $("#" + plugin).data("extraParameters")
						    				});
				    					$("#" + jPageNoId).html("Showing page " + $("#" + plugin).data("pageNo") + " of " + $("#" + plugin).data("pageCount") + " pages.");
				    				}
				    			});
				    			
				    			$(document).on('click', "#" + jPreviousButtonId, function() {
				    				if($("#" + plugin).data("pageNo") == 1) {
				    					if(!$("#" + jPreviousButtonId).hasClass("disabled")) {
				    						$("#" + jPreviousButtonId).addClass("disabled");
				    					}
				    				} else {
				    					if($("#" + jPreviousButtonId).hasClass("disabled")) {
				    						$("#" + jPreviousButtonId).removeClass("disabled");
				    					}
				    					$("#" + plugin).data("pageNo", $("#" + plugin).data("pageNo")-1);
				    					if($("#" + plugin).data("pageNo") == 1) {
					    					if(!$("#" + jPreviousButtonId).hasClass("disabled")) {
					    						$("#" + jPreviousButtonId).addClass("disabled");
					    					}
					    				}
				    					if($("#" + jNextButtonId).hasClass("disabled")) {
				    						$("#" + jNextButtonId).removeClass("disabled");
				    					}
				    					$("#" + plugin).jPagination({
					    					basePath: $("#" + plugin).data("basePath"),
					    					order: $("#" + plugin).data("order"),
					    					pageNo: $("#" + plugin).data("pageNo"),
					    					limit: $("#" + plugin).data("limit"),
					    					pageCount: $("#" + plugin).data("pageCount"),
					    					source: $("#" + plugin).data("source"),
					    					sorting: $("#" + plugin).data("sorting"),
					    					filter: $("#" + plugin).data("filter"),
					    					filtersApplied: $("#" + plugin).data("filtersApplied"),
					    					extraParameters: $("#" + plugin).data("extraParameters")
					    				});
				    					$("#" + jPageNoId).html("Showing page " + $("#" + plugin).data("pageNo") + " of " + $("#" + plugin).data("pageCount") + " pages.");
				    				}
				    			});
				    		}
			    	},
			    	complete: function() {
			    		$("#loader").hide();
			    	},
			    	error: function(data) {
			    		alert("Something went wrong. Please try again.");	
			    	}
			    });
			
			});

		}

	});
})(jQuery);


/*(function($){
	jQuery.fn.extend({

		jPagination: function(options) {
			
			var defaults = {  
				basePath: jBasePath,
				order: jOrder,
				pageNo: jPageNo,
				limit: jLimit,
				pageCount: jPageCount,
				source: jSource,
				sorting: jSorting,
				filter: jFilterOptions,
				filtersApplied: jFilters,
				extraParameters: jExtraParameters
				
				basePath: "",
				order: 0,
				pageNo: 1,
				limit: 15,
				pageCount: 0,
				source: "",
				sorting: "",
				filter: "",
				filtersApplied: [],
				extraParameters: ""
			};  
			var options = $.extend(defaults, options);  
			
			var plugin = this;
			
			jBasePath = options.basePath;
			jOrder = options.order;
			jPageNo = options.pageNo;
			jLimit = options.limit;
			jPageCount = options.pageCount;
			jSource = options.source;
			jSorting = options.sorting;
			jFilterOptions = options.filter;
			jFilters = options.filtersApplied;
			jExtraParameters = options.extraParameters;
			
			plugin.data("basePath", options.basePath);
			plugin.data("order", options.order);
			plugin.data("pageNo", options.pageNo);
			plugin.data("limit", options.limit);
			plugin.data("pageCount", options.pageCount);
			plugin.data("source", options.source);
			plugin.data("sorting", options.sorting);
			plugin.data("filter", options.filter);
			plugin.data("filtersApplied", options.filtersApplied);
			plugin.data("extraParameters", options.extraParameters);
			
			return this.each(function() {
				
					var jSortingId = this.id + "-sorting-options";
					var jTableId = this.id + "-table";
					var jPaginationId = this.id + "-pagination";
					var jNextButtonId = this.id + "-nextButton";
					var jPreviousButtonId = this.id + "-previousButton";
					var jPageNoId = this.id + "-pages";
					var jFilterId = this.id + "-filter";
				
				if(this != divId) {
					jBegin = true;
					divId = plugin;
					$("#" + this.id).html("<div class=\"sorting-options\" id=\"" + jSortingId + "\"></div><div class=\"sorting-options\" id=\"" + jFilterId + "\"></div><div id=\"" + jTableId + "\"></div><div id=\"" + jPaginationId + "\"></div>");
				}
				$("#" + jTableId).html('<img id="loader" style="display:block;margin:auto" src="'+jBasePath+'/assets/img/loader.gif">');
				var jData = {};
				if(jOrder == 0) {
					if(jFilters == [] || jFilters.length === 0) {
						jData = {
					    		'pageNo': jPageNo,
					    		'limit': jLimit,
					    		'begin': jBegin,
					    		'isFiltered': false,
					    		'extraParameters': jExtraParameters
						};
					} else {
						jData = {
					    		'pageNo': jPageNo,
					    		'limit': jLimit,
					    		'begin': jBegin,
					    		'filters': jFilters,
					    		'isFiltered': true,
					    		'extraParameters': jExtraParameters
						};
					}
				} else {
					if(jFilters == [] || jFilters.length === 0) {
						jData = {
								'order': jOrder,
					    		'pageNo': jPageNo,
					    		'limit': jLimit,
					    		'begin': jBegin,
					    		'isFiltered': false,
					    		'extraParameters': jExtraParameters
						};
					} else {
						jData = {
								'order': jOrder,
					    		'pageNo': jPageNo,
					    		'limit': jLimit,
					    		'begin': jBegin,
					    		'filters': jFilters,
					    		'isFiltered': true,
					    		'extraParameters': jExtraParameters
						};
					}
				}
				$.ajax({
			    	url: jSource,
			    	data: jData,
			    	dataType: 'json',	
			    	method: "POST",
			    	beforeSend: function() {
			    		$("#" + jTableId).html('<img id="loader" style="display:block;margin:auto" src="'+jBasePath+'/assets/img/loader.gif">');
			    	},
			    	success: function(data) {
			    		
				    		$("#" + jTableId).html(data.table);
				    		if(jBegin && data.pageCount != 0) {
				    			jBegin = false;
				    			jPageCount = data.pageCount;
				    			//if(jOrder != 0) {
				    			if(jSorting != "" || jSorting.length !== 0) {
				    				var sortingList = '<select class=\"btn btn-default sort-dropdown\" id="'+jSortingId+'-select">';
					    			sortingList += '<option selected disabled hidded>Sort results by</options>';
					    			for(var i = 0; i < jSorting.length; i++) {
					    			    sortingList += '<option value="' + jSorting[i][0] + '">' + jSorting[i][1]  + '</option>'; 
					    			}
					    			sortingList += '</select>';
					    			$("#" + jSortingId).html(sortingList);
					    			
					    			$(document).on('change', '#'+jSortingId+"-select", function() {
					    				jOrder = this.value;
					    				$(divId).jPagination();
					    			});
				    			}
				    			
				    			if(jFilterOptions != "" || jFilterOptions.length !== 0) {
				    				var filterList = '<select id="'+jFilterId+'-select" multiple="multiple">';
					    			filterList += '<option selected disabled hidded>Filter results by</options>';
					    			for(var i = 0; i < jFilterOptions.length; i++) {
					    			    filterList += '<option value="' + jFilterOptions[i][0] + '">' + jFilterOptions[i][1]  + '</option>'; 
					    			}
					    			filterList += '</select>';
					    			$("#" + jFilterId).html(filterList);
					    			$("#" + jFilterId + "-select").multiselect({
					    				   
					    				   onChange: function(element, checked) {
					    		               if (checked === true) {
					    		            	   jFilters.push(element.val());
					    		               } else if (checked === false) {
					    		            	   var index = jFilters.indexOf(element.val());
					    		            	   if (index > -1) {
					    		            		   jFilters.splice(index, 1);
					    		            	   }
					    		               } 
					    		               $(divId).jPagination();
					    		           } 
					    				   
					    			   }); 
				    			}
				    			
				    			var navButtons = "<nav aria-label=\"pagination\"><ul class=\"pagination justify-content-center\">";
				    			navButtons += "<li style=\"cursor:pointer\" class=\"page-item disabled previousButton\" id=\""+ jPreviousButtonId +"\"><a class=\"page-link\">Previous</a></li>";
				    			if(jPageCount == 1) {
				    				navButtons += "<li class=\"page-item disabled nextButton\" id=\""+ jNextButtonId +"\" style=\"cursor:pointer\"><a class=\"page-link\">Next</a></li></ul></nav>";
				    			} else {
				    				navButtons += "<li class=\"page-item nextButton\" id=\""+ jNextButtonId +"\"  style=\"cursor:pointer\"><a class=\"page-link\">Next</a></li></ul>";
				    			}
				    			navButtons += "<div id=\""+jPageNoId+"\">Showing page " + jPageNo + " of " + jPageCount + " pages.</div>";
				    			$("#" + jPaginationId).html(navButtons);
				    			
				    			
				    			
				    			$(document).on('click', '#' + jNextButtonId, function() {
				    				if(jPageNo == jPageCount) {
				    					if(!$("#" + jNextButtonId).hasClass("disabled")) {
				    						$("#" + jNextButtonId).addClass("disabled");
				    					}
				    				} else {
				    					if($("#" + jNextButtonId).hasClass("disabled")) {
				    						$("#" + jNextButtonId).removeClass("disabled");
				    					}
				    					jPageNo += 1;
				    					if(jPageNo == jPageCount) {
					    					if(!$("#" + jNextButtonId).hasClass("disabled")) {
					    						$("#" + jNextButtonId).addClass("disabled");
					    					}
					    				}
				    					if($("#" + jPreviousButtonId).hasClass("disabled")) {
				    						$("#" + jPreviousButtonId).removeClass("disabled");
				    					}
				    					$(divId).jPagination();
				    					$("#" + jPageNoId).html("Showing page " + jPageNo + " of " + jPageCount + " pages.");
				    				}
				    			});
				    			
				    			$(document).on('click', '#' + jPreviousButtonId, function() {
				    				if(jPageNo == 1) {
				    					if(!$("#" + jPreviousButtonId).hasClass("disabled")) {
				    						$("#" + jPreviousButtonId).addClass("disabled");
				    					}
				    				} else {
				    					if($("#" + jPreviousButtonId).hasClass("disabled")) {
				    						$("#" + jPreviousButtonId).removeClass("disabled");
				    					}
				    					jPageNo -= 1;
				    					if(jPageNo == 1) {
					    					if(!$("#" + jPreviousButtonId).hasClass("disabled")) {
					    						$("#" + jPreviousButtonId).addClass("disabled");
					    					}
					    				}
				    					if($("#" + jNextButtonId).hasClass("disabled")) {
				    						$("#" + jNextButtonId).removeClass("disabled");
				    					}
				    					$(divId).jPagination();
				    					$("#" + jPageNoId).html("Showing page " + jPageNo + " of " + jPageCount + " pages.");
				    				}
				    			});
				    		}
			    	},
			    	complete: function() {
			    		$("#loader").hide();
			    	},
			    	error: function(data) {
			    		alert("Something went wrong. Please try again.");	
			    	}
			    });
			
			});

		}

	});
})(jQuery); */

/*

function pagination(basepath, order, pageNo, limit, pageCount) {
	$.ajax({
    	url: basepath + "admin/home?action=getUserList",
    	data: {
    		'order': order,
    		'pageNo': pageNo,
    		'limit': limit,
    		'pageCount': pageCount,
    	},
    	method: "POST",
    	beforeSend: function() {
    		document.getElementById("userTable").innerHTML = '<img id="loader" style="display:block;margin:auto" src="'+basepath+'/assets/img/loader.gif">';
    		if(pageNo == 1) {
    			document.getElementById("pages").innerHTML = '<img id="pagesloader" style="height:35px" src="'+basepath+'/assets/img/loader.gif">';
    			document.getElementById("sorting-options").innerHTML = '<img id="sortingloader" style="height:35px" src="'+basepath+'/assets/img/loader.gif">';
    		}
    	},
    	success: function(data) {
    		if(data == "0") {
	    		document.getElementById("userTable").innerHTML = "There are no registered users yet.";
	    	} else {
	    		document.getElementById("userTable").innerHTML = data;
	    		if(pageNo == 1) {
	    			$.ajax({
			    		url: basepath + "admin/home?action=doPagination",
			    		method: "POST",
			    		success: function(pageData) {
			    			document.getElementById("pages").innerHTML = pageData;
			    			var sorting = '<select>';
			    			sorting += '<option selected disabled hidded>Sort results by</options>';
			    			sorting += '<option value="1">Candidate Id (low to high)</option>';
			    			sorting += '<option value="2">Candidate Id (high to low)</option>';
			    			sorting += '<option value="3">Candidate Name (low to high)</option>';
			    			sorting += '<option value="4">Candidate Name (high to low)</option>';
			    			sorting += "</select>";
			    			document.getElementById("sorting-options").innerHTML = sorting;
			    		},
			    		complete: function() {
			    			$("#pagesloader").hide();
			    			$("#sortingloader").hide();
			    		},
			    		error: function(pageData) {
			    			alert("Something went wrong. Please try again.");
			    		}
			    	});
	    		}
	    	}
    	},
    	complete: function() {
    		$("#loader").hide();
    	},
    	error: function(data) {
    		alert("Something went wrong. Please try again.");	
    	}
    });
}

*/