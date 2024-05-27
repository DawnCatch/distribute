<template>
    <div class="test">
        <div style="display: flex;align-items: flex-end;">
            <div>
                <div v-for="i in 100">
                    {{ i }}
                </div>
            </div>
            <div class="h">
                头1
            </div>
        </div>
        <div>
            <div>
                <div v-for="i in 100">
                    {{ i }}
                </div>
            </div>
            <div class="h">
                头2
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">

</script>

<style scoped>
.test {
    /* display: flex; */
    width: 80%;
    height: 100%;
    overflow: auto;
}

.h {
    position: sticky;
    bottom: 0;
}
</style>