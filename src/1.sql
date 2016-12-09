update consult_content a set a.content = '是否有肝病史、乙型肝炎表面抗原阳性、HCV抗体阳性。' where a.itemIndex = 3 and a.type = '0';
update consult_content a set a.content = '是否有器质性神经系统疾患或精神病（如脑炎、脑外伤后遗症、癫痫、精神分裂症、癔症、严重神经衰弱等）。' where a.itemIndex = 12 and a.type = '0';
update consult_content a set a.content = '是否有寄生虫病及地方病（如黑热病、血吸虫病、丝虫病、钩虫病、绦虫病、肺吸虫病、克山病、大骨节病等）。' where a.itemIndex = 13 and a.type = '0';
update consult_content a set a.content = '是否患克雅病（克罗依茨-雅克布病，CJD）和变异性克雅病（vCJD）及有家族病史者，接受过人和动物脑垂体来源物质（如生长激素、促性激素、甲状腺刺激素等）治疗，接受器官（含角膜、骨髓、硬脑膜）移植。是否暴露于牛海绵状脑病（BSE）和vCJD。' where a.itemIndex = 18 and a.type = '0';
COMMIT;