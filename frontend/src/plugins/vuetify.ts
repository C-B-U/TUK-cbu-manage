// src/plugins/vuetify.js
import '@/styles/main.scss';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

export default createVuetify({
    components,
    directives,
    defaults: {
        global: {
            style: {
                fontFamily: 'Pretendard, sans-serif', // 글로벌 폰트 설정
            },
        },
    },
});
