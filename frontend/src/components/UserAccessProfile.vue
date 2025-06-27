<template>
    <div>
        <Boolean
            label="IsSubscribed"
            v-model="value.isSubscribed"
            :editMode="editMode"
        />
        <Number
            label="Points"
            v-model="value.points"
            :editMode="editMode"
        />
        <Date
            label="SubscribtionDue"
            v-model="value.subscribtionDue"
            :editMode="editMode"
        />
        <v-row class="ma-0 pa-0">
            <v-spacer></v-spacer>
            <v-btn width="64px" color="primary" @click="save">
                저장
            </v-btn>
        </v-row>
    </div>
</template>


<script>
import BaseEntity from './base-ui/BaseEntity.vue'

export default {
    name: 'UserAccessProfile',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "userAccessProfiles",
        value: {
        }
    }),
    created(){
    },
    computed:{
    },
    methods: {
    },
}
</script>
