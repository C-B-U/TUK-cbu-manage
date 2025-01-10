<template>
    <div class="container">
        <header class="header">
            <div class="left-header">
                <span class="total-count">전체 동아리 명부 {{ data.length }}명</span>
            </div>
            <div class="right-header">
                <button class="sync-button">스프레드 시트 연동하기</button>
                <input type="text" placeholder="이름을 검색하세요." class="search-input" />
            </div>
        </header>

        <table class="table">
            <thead>
                <tr>
                    <th>선택</th>
                    <th class="filter-header">
                        기수 <button @click="toggleDropdown" class="filter-button">▼</button>
                        <div class="dropdown">
                            <ul v-if="dropdownVisible" class="dropdown-menu">
                                <li v-for="batch in batchList" :key="batch" @click="filterByBatch(batch)">
                                    {{ batch }}
                                </li>
                                <li @click="resetFilter" class="reset">전체 보기</li>
                            </ul>
                        </div>
                    </th>
                    <th>이름</th>
                    <th>학번</th>
                    <th>닉네임</th>
                    <th>메일 주소</th>
                    <th>입금 상태</th>
                    <th>활동 여부</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in paginatedData" :key="index">
                    <td>
                        <input
                            type="checkbox"
                            v-model="selectedItems"
                            :value="item.id"
                        />
                    </td>
                    <td>{{ item.batch }}</td>
                    <td>{{ item.name }}</td>
                    <td>{{ item.studentId }}</td>
                    <td>{{ item.nickname }}</td>
                    <td>{{ item.email }}</td>
                    <td>{{ item.paymentStatus ? "✔" : "✘" }}</td>
                    <td>{{ item.activityStatus }}</td>
                </tr>
            </tbody>
        </table>

        <div class="button-group">
            <div class="left-buttons">
                <button class="btn" @click="selectAllItems">전체선택</button>
                <button class="btn-outline" @click="clearSelection">선택취소</button>
            </div>
            <div class="right-buttons">
                <button class="btn">활동 상태 변경</button>
                <button class="btn">디스코드 일괄 초대링크 발송</button>
            </div>
        </div>

        <v-pagination v-model:currentPage="currentPage" :length="totalPages" class="pagination" />
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';

// 드롭다운 상태 관리
const dropdownVisible = ref(false);
const batchFilter = ref(null);

// 드롭다운 열기/닫기
const toggleDropdown = () => {
    dropdownVisible.value = !dropdownVisible.value;
};

// 기수 목록 생성
const batchList = Array.from({ length: 20 }, (_, i) => `${i + 1}기`);

// 더미 데이터 생성
const statuses = ["활동", "중단", "탈퇴"];
const data = Array.from({ length: 30 }, (_, i) => ({
    id: i + 1, // 고유 ID
    name: `회원 ${i + 1}`,
    studentId: `20191${Math.floor(10000 + Math.random() * 89999)}`,
    nickname: `닉네임 ${i + 1}`,
    email: `user${i + 1}@tukorea.ac.kr`,
    batch: `${Math.floor(1 + Math.random() * 20)}기`,
    paymentStatus: Math.random() > 0.5,
    activityStatus: statuses[Math.floor(Math.random() * statuses.length)], // 랜덤 활동 여부
}));

// 선택된 기수에 따라 데이터 필터링
const filteredData = computed(() => {
    if (batchFilter.value) {
        return data.filter((item) => item.batch === batchFilter.value);
    }
    return data;
});

// 페이지네이션 설정
const itemsPerPage = 10;
const currentPage = ref(1);
const totalPages = computed(() => Math.ceil(filteredData.value.length / itemsPerPage));

// 현재 페이지 데이터 계산
const paginatedData = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return filteredData.value.slice(start, end);
});

// 선택된 아이템 관리
const selectedItems = ref<number[]>([]);
const selectAll = ref(false);

// 현재 페이지의 모든 항목 선택/선택 취소
const toggleSelectAll = () => {
    if (selectAll.value) {
        // 현재 페이지의 모든 ID 추가
        selectedItems.value = paginatedData.value.map((item) => item.id);
    } else {
        // 현재 페이지의 ID 제거
        selectedItems.value = selectedItems.value.filter(
            (id) => !paginatedData.value.some((item) => item.id === id)
        );
    }
};

// 전체 선택 버튼
const selectAllItems = () => {
    selectedItems.value = data.map((item) => item.id);
};

// 선택 취소 버튼
const clearSelection = () => {
    selectedItems.value = [];
};

// 특정 기수로 필터링
const filterByBatch = (batch) => {
    batchFilter.value = batch;
    dropdownVisible.value = false;
};

// 필터 초기화
const resetFilter = () => {
    batchFilter.value = null;
    dropdownVisible.value = false;
};
</script>

<style scoped src="./MemManagePage.css"></style>
