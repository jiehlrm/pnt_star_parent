(function(w) {
	w.COMMON = {
		/**
		 * 同步ajax，获取数据用
		 * 
		 * @param {*}
		 *            data
		 */
		getSyncData : function(data) {
			var d = '';
			$.ajax({
				type : 'post',
				url : nowurl + "ControlServlet.do",
				data : data,
				async : false,
				dataType : 'json',
				success : function(data) {
					if (data.resultCode == '00000') {
						d = data.data;
					} else {
						layer.msg(data.resultMsg);
					}

				}
			});
			return d;
		},
		/**
		 * 同步ajax，进行某些事情的操作
		 * 
		 * @param {*}
		 *            data
		 */
		doSync : function(data,callback) {
			var d = '';
			$.ajax({
				type : 'post',
				url : nowurl + "ControlServlet.do",
				data : data,
				async : false,
				dataType : 'json',
				success : function(data) {
					if (data.resultCode == '00000') {
						callback(data);
					} else {
						layer.msg(data.resultMsg);
					}
				}
			});
		}
	};
})(window);