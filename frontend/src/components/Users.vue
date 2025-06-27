<template>
    <div>
        <String
            label="Nickname"
            v-model="value.nickname"
            :editMode="editMode"
        />
        <String
            label="AuthorshipStatus"
            v-model="value.authorshipStatus"
            :editMode="editMode"
        />
        <Date
            label="CreatedAt"
            v-model="value.createdAt"
            :editMode="editMode"
        />
        <Boolean
            label="Subscriber"
            v-model="value.subscriber"
            :editMode="editMode"
        />
        <String
            label="AccountId"
            v-model="value.accountId"
            :editMode="editMode"
        />
        <String
            label="Password"
            v-model="value.password"
            :editMode="editMode"
        />
        <String
            label="AuthorsProfile"
            v-model="value.authorsProfile"
            :editMode="editMode"
        />
        <String
            label="Autornickname"
            v-model="value.autornickname"
            :editMode="editMode"
        />
        <Boolean
            label="AgreedToMarketing"
            v-model="value.agreedToMarketing"
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
    name: 'Users',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "users",
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
